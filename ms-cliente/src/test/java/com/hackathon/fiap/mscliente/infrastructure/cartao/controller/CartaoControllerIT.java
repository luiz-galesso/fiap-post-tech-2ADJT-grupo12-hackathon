package com.hackathon.fiap.mscliente.infrastructure.cartao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.repository.CartaoRepository;
import com.hackathon.fiap.mscliente.infrastructure.cliente.repository.ClienteRepository;
import com.hackathon.fiap.mscliente.usecase.cartao.RegistraCartao;
import com.hackathon.fiap.mscliente.usecase.cliente.CadastraCliente;
import com.hackathon.fiap.mscliente.utils.CartaoHelper;
import com.hackathon.fiap.mscliente.utils.ClienteHelper;
import com.hackathon.fiap.mscliente.utils.GetAuth;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class CartaoControllerIT {


    @Mock
    ClienteGateway clienteGateway;
    @Mock
    CartaoGateway cartaoGateway;

    @Autowired
    CadastraCliente cadastrarCliente;
    @Autowired
    RegistraCartao registraCartao;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    CartaoRepository cartaoRepository;

    @LocalServerPort
    private int port;

    private String token;
    private Cliente cliente;
    private Cartao cartao;

    @BeforeEach
    void setup() throws JsonProcessingException {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        cliente = clienteRepository.save(ClienteHelper.gerarCliente());
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        cartao = cartaoRepository.save(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));
        token = GetAuth.getToken();
    }

    @Test
    void devePermitirCadastrarCartao() {
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(cartaoRequestDTO)
                .when()
                .post("/api/cartao")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void devePermitirConsumirLimite() {
        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.findById(any(String.class))).thenReturn(Optional.of(cartao));

        ConsomeLimiteRequestDTO consomeLimiteRequestDTO = new ConsomeLimiteRequestDTO(cliente.getCpf(), cartao.getNumeroCartao(), cartao.getDataValidade(), cartao.getCvv().toString(), 10D);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(consomeLimiteRequestDTO)
                .when()
                .put("/api/cartao/limite")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

}
