package com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto;

public record ConsomeLimiteRequestDTO(String cpf, String numero, String data_validade, String cvv, Double valor) {
}
