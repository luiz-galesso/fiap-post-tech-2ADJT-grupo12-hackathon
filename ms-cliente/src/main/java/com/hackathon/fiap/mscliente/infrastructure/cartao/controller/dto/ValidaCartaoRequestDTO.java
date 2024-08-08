package com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto;

public record ValidaCartaoRequestDTO(String cpf, String numero, String data_validade, String cvv) {
}
