package com.hackathon.fiap.mspagamento.infrastructure.feign.dto;

public record ConsomeLimiteResponseDTO(Integer codigoErro, String message, String numeroCartao, Double limiteRestante) {

}
