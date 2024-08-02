package com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto;

public record ConsomeLimiteResponseDTO(Integer codigoErro, String message, String numeroCartao, Double limiteRestante) {
}
