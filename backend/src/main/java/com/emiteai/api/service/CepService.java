package com.emiteai.api.service;

import com.emiteai.api.model.entity.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CepService {
    private final ViaCepClient client;

    public void preencherEndereco(Pessoa p) {
        var dto = client.consultaCep(p.getCep());
        p.setBairro(dto.getBairro());
        p.setCidade(dto.getLocalidade());
        p.setEstado(dto.getUf());
    }
}

