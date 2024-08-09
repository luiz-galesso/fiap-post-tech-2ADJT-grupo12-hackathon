package com.hackathon.fiap.mscliente.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;

import java.util.Random;

public class ClienteHelper {
    private static final Faker faker = new Faker();

    public static ClienteRequestDTO gerarClienteRequest() {
        String nome = faker.name().firstName();
        String sobreNome = faker.name().lastName();
        String email = faker.internet().emailAddress(nome + "." + sobreNome);
        String fone = faker.phoneNumber().phoneNumber();

        return new ClienteRequestDTO(generateCPF()
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

    private static String generateCPF() {
        Random random = new Random();

        int[] cpf = new int[11];
        for (int i = 0; i < 9; i++) {
            cpf[i] = random.nextInt(10);
        }

        cpf[9] = calculateDigit(cpf, 10);
        cpf[10] = calculateDigit(cpf, 11);

        StringBuilder cpfFormatted = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            cpfFormatted.append(cpf[i]);
            if (i == 2 || i == 5) {
                cpfFormatted.append('.');
            } else if (i == 8) {
                cpfFormatted.append('-');
            }
        }
        return cpfFormatted.toString();
    }

    private static int calculateDigit(int[] cpf, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < pesoInicial - 1; i++) {
            soma += cpf[i] * (pesoInicial - i);
        }
        int resto = 11 - (soma % 11);
        return (resto >= 10) ? 0 : resto;
    }


}
