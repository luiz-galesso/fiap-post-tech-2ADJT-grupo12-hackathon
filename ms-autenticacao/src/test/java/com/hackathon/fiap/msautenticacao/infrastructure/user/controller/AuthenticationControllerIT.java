package com.hackathon.fiap.msautenticacao.infrastructure.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hackathon.fiap.msautenticacao.infrastructure.user.controller.dto.AuthenticationDTO;
import com.hackathon.fiap.msautenticacao.usecase.user.AutenticaUsuario;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AuthenticationControllerIT {

    @Autowired
    AutenticaUsuario autenticaUsuario;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() throws JsonProcessingException {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void autenticaUsuario() throws Exception {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("adj2", "adj@1234");

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(authenticationDTO)
                .when()
                .post("/api/autenticacao")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

}
