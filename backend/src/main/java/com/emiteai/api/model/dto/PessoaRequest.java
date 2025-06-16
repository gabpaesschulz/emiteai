package com.emiteai.api.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequest {

    @NotBlank @Size(max=150)
    private String nome;

    @NotBlank @Size(max=20)
    private String telefone;

    @Pattern(regexp="\\d{11}", message="CPF deve conter 11 dígitos")
    private String cpf;

    @Pattern(regexp="\\d{8}", message="CEP deve conter 8 dígitos")
    private String cep;

    @NotBlank @Size(max=10)
    private String numero;

    @Size(max=60)
    private String complemento;

}
