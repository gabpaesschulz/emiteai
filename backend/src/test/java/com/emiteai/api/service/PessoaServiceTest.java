package com.emiteai.api.service;

import com.emiteai.api.exception.DuplicateCpfException;
import com.emiteai.api.mapper.PessoaMapper;
import com.emiteai.api.model.dto.PessoaRequest;
import com.emiteai.api.model.dto.PessoaResponse;
import com.emiteai.api.model.entity.Pessoa;
import com.emiteai.api.repository.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock private PessoaRepository repo;
    @Mock private PessoaMapper mapper;
    @Mock private CepService cepService;

    @InjectMocks
    private PessoaService service;

    @Test
    void create_retornaResponse_quandoOk() {
        PessoaRequest req = new PessoaRequest();
        req.setNome("Ana");

        Pessoa entity = new Pessoa();
        entity.setId(1L);
        entity.setNome("Ana");

        when(mapper.toEntity(req)).thenReturn(entity);
        doAnswer(inv -> null).when(cepService).preencherEndereco(entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(
                new PessoaResponse(
                        1L, "Ana",
                        null, null, null,
                        null, null, null, null, null));

        PessoaResponse resp = service.create(req);

        assertThat(resp.getId()).isEqualTo(1L);
        verify(repo).save(entity);
        verify(cepService).preencherEndereco(entity);
    }

    @Test
    void create_lancaDuplicateCpf_quandoCpfJaExiste() {
        PessoaRequest req = new PessoaRequest();
        req.setCpf("12345678901");

        when(mapper.toEntity(req)).thenReturn(new Pessoa());
        doAnswer(inv -> null).when(cepService).preencherEndereco(any());

        SQLException sql = new SQLException("duplicate key", "23505");
        DataIntegrityViolationException dbEx =
                new DataIntegrityViolationException("erro", sql);
        when(repo.save(any())).thenThrow(dbEx);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(DuplicateCpfException.class);
    }
}
