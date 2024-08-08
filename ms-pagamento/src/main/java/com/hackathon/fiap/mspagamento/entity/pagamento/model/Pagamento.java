package com.hackathon.fiap.mspagamento.entity.pagamento.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_pagamento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue
    private UUID id;

    private String cpf;
    private String numberoCartao;
    private Double valor;
    private LocalDateTime localDateTime;
    private String descricao;
    private String metodoPagamento;
    private String status;

}
