package com.hackathon.fiap.mspagamento.infrastructure.pagamento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.fiap.mspagamento.infrastructure.feign.CartaoClient;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.usecase.pagamento.ObtemPagamentosPorCliente;
import com.hackathon.fiap.mspagamento.usecase.pagamento.RealizaPagamento;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class PagamentoControllerIT {

    @Autowired
    RealizaPagamento realizaPagamento;

    @Autowired
    ObtemPagamentosPorCliente obtemPagamentosPorCliente;

    @MockBean
    CartaoClient cartaoClient;

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void setup() throws JsonProcessingException {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        token = getToken();
    }

    private String getToken() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap<String, String> request = new HashMap<>();
        request.put("usuario", "adj2");
        request.put("senha", "adj@1234");

        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8081" + "/api/autenticacao",
                HttpMethod.POST,
                entity,
                String.class
        );
        return getBodyTokenResponse(response.getBody());
    }

    @Test
    void devePermitirRealizarPagamento() {

        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        when(cartaoClient.consomeLimite(anyString(), any(ConsomeLimiteRequestDTO.class)))
                .thenReturn(PagamentoHelper.gerarConsomeLimiteResponseDTO());

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + token)
            .body(pagamentoRequestDTO)
        .when()
            .post("/api/pagamentos")
        .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    void devePermitirObterTodosOsPagamentosDeUmCliente() {

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/pagamentos/cliente/{chave}", "12345678909")
        .then()
                .statusCode(200);
    }


    public String getBodyTokenResponse(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(body);
        return jsonNode.path("token").asText();
    }

}
