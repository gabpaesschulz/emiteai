package com.emiteai.api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "audit_log")
@Getter @Setter
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rota;
    private String metodo;

    @Column(columnDefinition = "TEXT")
    private String corpo;

    private String ip;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "criado_em")
    private Instant criadoEm = Instant.now();
}

