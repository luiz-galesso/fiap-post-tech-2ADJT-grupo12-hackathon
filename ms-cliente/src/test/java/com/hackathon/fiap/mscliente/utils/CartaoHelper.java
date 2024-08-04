package com.hackathon.fiap.mscliente.utils;

import com.github.javafaker.Faker;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;

public class CartaoHelper {
    private static final Faker faker = new Faker();

    public static CartaoRequestDTO gerarClienteRequest(String cpf) {
        Double limite = faker.number().randomDouble(2, 1, 10000);
        String numero = faker.number().digits(10);
        String cvv = faker.number().digits(3);

        return new CartaoRequestDTO(cpf
                , limite
                , numero
                , "30-2030"
                , cvv
        );
    }
}
