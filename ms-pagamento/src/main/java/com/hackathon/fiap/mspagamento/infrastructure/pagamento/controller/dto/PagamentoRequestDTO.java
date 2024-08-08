package com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto;

public record PagamentoRequestDTO(String cpf, String numero, String data_validade, String cvv, Double valor) {
}


