package com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto;

public record PagamentoResponseDTO(Double valor, String descricao, String metodo_pagamento, String status) {
}
