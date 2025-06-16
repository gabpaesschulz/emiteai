package com.emiteai.api.controller;

import com.emiteai.api.model.dto.RelatorioResponse;
import com.emiteai.api.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService service;

    @PostMapping
    public ResponseEntity<RelatorioResponse> gerar() {
        RelatorioResponse dto = service.solicitarGeracao();
        return ResponseEntity.accepted().body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatorioResponse> get(@PathVariable Long id) {
        RelatorioResponse dto = service.consultar(id);
        return (dto == null) ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        FileSystemResource file = service.arquivo(id);
        if (!file.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%s\"".formatted(file.getFilename()))
                .body(file);
    }
}
