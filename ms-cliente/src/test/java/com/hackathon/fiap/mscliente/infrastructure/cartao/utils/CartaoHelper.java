package com.hackathon.fiap.mscliente.infrastructure.cartao.utils;

import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.repository.CartaoRepository;

public class CartaoHelper {
    public static Cartao gerarCartao(String id) {
        return Cartao.builder()
                .numeroCartao(id)
                .limite(700.00)
                .dataValidade("08/30")
                .cvv(351)
                .cliente(Cliente.builder()
                        .cpf("454.118.542-37")
                        .nome("Adalberto Pereira")
                        .email("adalberto.pereira@yahoo.com")
                        .telefone("74 99785-1050")
                        .rua("Rua das Palmeiras Verdes, 761")
                        .cidade("Jaboatão dos caraibas")
                        .estado("Amapá")
                        .cep("91.741-000")
                        .pais("Brasil").build()).build();
    }

    public static Cartao registrarCartao(CartaoRepository cartaoRepository, Cartao cartao) {
        return cartaoRepository.save(cartao);
    }
}
