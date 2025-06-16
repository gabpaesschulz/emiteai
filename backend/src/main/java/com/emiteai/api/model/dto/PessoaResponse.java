package com.emiteai.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponse {

    private Long   id;
    private String nome;
    private String telefone;
    private String cpf;
    private String cep;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
}
