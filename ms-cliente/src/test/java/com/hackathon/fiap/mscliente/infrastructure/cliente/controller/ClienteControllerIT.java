package com.hackathon.fiap.mscliente.infrastructure.cliente.controller;

import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.usecase.cliente.CadastraCliente;
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
class ClienteControllerIT {

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
    void devePermitirCadastrarCliente() {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clienteRequestDTO)
                .when()
                .post("/api/cliente")
                .then()
                .log().all();
                //.statusCode(HttpStatus.CREATED.value());
    }

}
