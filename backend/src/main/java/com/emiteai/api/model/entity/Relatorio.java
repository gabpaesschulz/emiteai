package com.emiteai.api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name = "relatorio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Relatorio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caminho;
    private String status;

    @Column(name = "criado_em")
    private Instant criadoEm = Instant.now();

    @Column(name = "pronto_em")
    private Instant prontoEm;
}
