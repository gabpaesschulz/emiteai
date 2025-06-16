package com.emiteai.api.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ViaCepClient {

    private final RestClient rest = RestClient.create();

    public EnderecoDTO consultaCep(String cep) {
        var url = "https://viacep.com.br/ws/{cep}/json/";
        var dto = rest.get().uri(url, cep).retrieve().body(EnderecoDTO.class);

        if (dto == null || dto.isErro())
            throw new IllegalArgumentException("CEP inválido");

        return dto;
    }

    @lombok.Data
    public static class EnderecoDTO {
        private String bairro;
        private String localidade;
        private String uf;
        private Boolean erro;
        public boolean isErro() { return Boolean.TRUE.equals(erro); }
    }
}
