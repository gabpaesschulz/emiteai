package com.emiteai.api.service;

import com.emiteai.api.exception.DuplicateCpfException;
import com.emiteai.api.mapper.PessoaMapper;
import com.emiteai.api.model.dto.PessoaRequest;
import com.emiteai.api.model.dto.PessoaResponse;
import com.emiteai.api.model.entity.Pessoa;
import com.emiteai.api.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PessoaService {

    private final PessoaRepository repo;
    private final PessoaMapper     mapper;
    private final CepService       cepService;

    public PessoaResponse create(PessoaRequest dto) {
        try {
            Pessoa entity = mapper.toEntity(dto);
            cepService.preencherEndereco(entity);
            return mapper.toResponse(repo.save(entity));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateCpfException();
        }
    }

    public List<PessoaResponse> list() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

}

