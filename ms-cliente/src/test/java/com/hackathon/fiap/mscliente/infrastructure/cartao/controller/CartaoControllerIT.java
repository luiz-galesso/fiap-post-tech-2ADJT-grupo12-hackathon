package com.hackathon.fiap.mscliente.infrastructure.cartao.controller;

import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.usecase.cliente.CadastraCliente;
import com.hackathon.fiap.mscliente.utils.CartaoHelper;
import com.hackathon.fiap.mscliente.utils.ClienteHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class CartaoControllerIT {

    @Autowired
    CadastraCliente cadastrarCliente;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @WithMockUser(username = "test", roles = {"admin"})
    void devePermitirCadastrarCartao() {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();
        cadastrarCliente.execute(clienteRequestDTO);
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(clienteRequestDTO.cpf());

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartaoRequestDTO)
                .when()
                .post("/api/cartao")
                .then()
                .log().all();
        //.statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @WithMockUser(username = "test", roles = {"admin"})
    void devePermitirConsumirLimite() {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(clienteRequestDTO.cpf());
        ConsomeLimiteRequestDTO consomeLimiteRequestDTO = new ConsomeLimiteRequestDTO(clienteRequestDTO.cpf(), cartaoRequestDTO.numero(), cartaoRequestDTO.data_validade(), cartaoRequestDTO.cvv(), 10D);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(consomeLimiteRequestDTO)
                .when()
                .put("/api/cartao/limite")
                .then()
                .log().all();
        //.statusCode(HttpStatus.CREATED.value());
    }

}
