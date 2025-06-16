package com.emiteai.api.controller;

import com.emiteai.api.model.dto.PessoaRequest;
import com.emiteai.api.model.dto.PessoaResponse;
import com.emiteai.api.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService service;

    @PostMapping
    public ResponseEntity<PessoaResponse> create(@Valid @RequestBody PessoaRequest dto) {
        PessoaResponse body = service.create(dto);
        return ResponseEntity.status(201).body(body);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponse>> list() {
        return ResponseEntity.ok(service.list());
    }
}
