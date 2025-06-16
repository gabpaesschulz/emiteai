package com.emiteai.api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pessoa")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
