package com.hackathon.fiap.mscliente.infrastructure.cliente.utils;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.repository.ClienteRepository;

public class ClienteHelper {
    public static Cliente gerarCliente(String id) {
        return Cliente.builder()
                .cpf(id)
                .nome("Adalberto Pereira")
                .email("adalberto.pereira@yahoo.com")
                .telefone("74 99785-1050")
                .rua("Rua das Palmeiras Verdes, 761")
                .cidade("Jaboatão dos caraibas")
                .estado("Amapá")
                .cep("91.741-000")
                .pais("Brasil").build();
    }
    public static Cliente registrarCliente(ClienteRepository clienteRepository, Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
