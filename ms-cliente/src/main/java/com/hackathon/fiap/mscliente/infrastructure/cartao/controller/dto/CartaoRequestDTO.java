package com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto;

public record CartaoRequestDTO(String cpf, Double limite, String numero, String data_validade, String cvv) {
}
