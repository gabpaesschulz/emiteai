package com.emiteai.api.service;

import com.emiteai.api.config.RabbitConfig;
import com.emiteai.api.model.dto.RelatorioResponse;
import com.emiteai.api.model.entity.Relatorio;
import com.emiteai.api.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final RelatorioRepository repo;
    private final RabbitTemplate rabbit;

    public RelatorioResponse solicitarGeracao() {
        Relatorio rel = new Relatorio();
        rel.setStatus("PENDENTE");
        rel = repo.save(rel);
        rabbit.convertAndSend(RabbitConfig.EXCHANGE_RELATORIO, "", rel.getId());
        return toDto(rel);
    }

    public RelatorioResponse consultar(Long id) {
        return repo.findById(id).map(this::toDto).orElse(null);
    }

    private RelatorioResponse toDto(Relatorio r) {
        return new RelatorioResponse(r.getId(), r.getStatus(), r.getCaminho(),
                r.getCriadoEm(), r.getProntoEm());
    }

    public FileSystemResource arquivo(Long id) {
        return repo.findById(id)
                .map(Relatorio::getCaminho)
                .map(FileSystemResource::new)
                .orElseGet(() -> new FileSystemResource(""));
    }


}
