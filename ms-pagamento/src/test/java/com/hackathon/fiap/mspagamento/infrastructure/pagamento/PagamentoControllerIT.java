package com.hackathon.fiap.mspagamento.infrastructure.pagamento;

import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.usecase.pagamento.ObtemPagamentosPorCliente;
import com.hackathon.fiap.mspagamento.usecase.pagamento.RealizaPagamento;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class PagamentoControllerIT {

    @Autowired
    RealizaPagamento realizaPagamento;

    @Autowired
    ObtemPagamentosPorCliente obtemPagamentosPorCliente;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    void devePermitirRealizarPagamento() {

        String token = "eyJraWQiOiJhZGoyIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJtcy1hdXRlbnRpY2FjYW8iLCJzdWIiOiJhZGoyIiwiUk9MRVMiOiJbUk9MRV9JTlRFR1JBVElPTl0iLCJleHAiOjE3MjI4MDc2NjZ9.9amC42IS9f0jAD7Fpsc09tvl1c8EP8jipEvkw9sEATE";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(pagamentoRequestDTO)
        .when()
            .post("/api/pagamentos")
        .then()
                .log().all();
    }

    @Test
    void devePermitirObterTodosOsPagamentosDeUmCliente() {
        String token = "eyJraWQiOiJhZGoyIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJtcy1hdXRlbnRpY2FjYW8iLCJzdWIiOiJhZGoyIiwiUk9MRVMiOiJbUk9MRV9JTlRFR1JBVElPTl0iLCJleHAiOjE3MjI4MDc2NjZ9.9amC42IS9f0jAD7Fpsc09tvl1c8EP8jipEvkw9sEATE";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        given()
                .contentType(ContentType.JSON)
        .header("Authorization", "Bearer " + token)
            .body(pagamentoRequestDTO)
        .when()
            .get("/api/pagamentos/clientes/{chave}", "12345678909")
        .then()
            .log().all();
    }

}
