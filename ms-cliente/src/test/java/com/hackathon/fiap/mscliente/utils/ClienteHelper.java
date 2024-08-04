package com.hackathon.fiap.mscliente.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;

public class ClienteHelper {
    private static final Faker faker = new Faker();

    public static ClienteRequestDTO gerarClienteRequest() {
        String nome = faker.name().firstName();
        String sobreNome = faker.name().lastName();
        String email = faker.internet().emailAddress(nome + "." + sobreNome);
        String fone = faker.phoneNumber().phoneNumber();

        return new ClienteRequestDTO("515.330.140-00"
                , nome
                , email
                , fone
                , "Olimpíadas "
                , "São Paulo"
                , "SP"
                , "04551-000"
                , "Brasil");
    }

    public static Cliente gerarCliente() {
        ClienteRequestDTO clienteRequestDTO = gerarClienteRequest();
        return new Cliente(clienteRequestDTO.cpf()
                , clienteRequestDTO.nome()
                , clienteRequestDTO.email()
                , clienteRequestDTO.telefone()
                , clienteRequestDTO.rua()
                , clienteRequestDTO.cidade()
                , clienteRequestDTO.estado()
                , clienteRequestDTO.cep()
                , clienteRequestDTO.pais());
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }

}
