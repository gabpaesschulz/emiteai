package com.emiteai.api.service;

import com.emiteai.api.config.RabbitConfig;
import com.emiteai.api.model.entity.Relatorio;
import com.emiteai.api.repository.RelatorioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock  private RelatorioRepository repo;
    @Mock  private RabbitTemplate rabbit;

    @InjectMocks
    private RelatorioService service;

    @Test
    void solicitarGeracao_salvaComoPendente_ePublicaId() {
        Relatorio salvo = new Relatorio();
        salvo.setId(10L);
        salvo.setStatus("PENDENTE");

        when(repo.save(any(Relatorio.class))).thenReturn(salvo);

        var dto = service.solicitarGeracao();

        assertThat(dto.status()).isEqualTo("PENDENTE");
        verify(rabbit).convertAndSend(
                eq(RabbitConfig.EXCHANGE_RELATORIO), eq(""), eq(10L));
    }
}
