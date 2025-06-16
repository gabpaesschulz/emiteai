package com.emiteai.api.model.dto;

import java.time.Instant;

public record RelatorioResponse(
        Long id,
        String status,
        String caminho,
        Instant criadoEm,
        Instant prontoEm
) {}
