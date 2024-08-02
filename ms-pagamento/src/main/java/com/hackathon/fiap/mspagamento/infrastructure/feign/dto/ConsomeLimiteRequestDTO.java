package com.hackathon.fiap.mspagamento.infrastructure.feign.dto;


public record ConsomeLimiteRequestDTO(String cpf, String numero, String data_validade, String cvv, Double valor) {

}
