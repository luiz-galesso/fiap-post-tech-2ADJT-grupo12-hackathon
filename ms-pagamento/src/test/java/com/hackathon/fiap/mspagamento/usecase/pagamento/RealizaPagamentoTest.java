package com.hackathon.fiap.mspagamento.usecase.pagamento;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.usecase.exception.BussinessErrorException;
import com.hackathon.fiap.mspagamento.usecase.exception.LimitErrorException;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hackathon.fiap.mspagamento.entity.pagamento.gateway.PagamentoGateway;
import com.hackathon.fiap.mspagamento.usecase.cartao.ValidaCartao;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RealizaPagamentoTest {

    AutoCloseable openMocks;

    private RealizaPagamento realizaPagamento;

    @Mock
    private PagamentoGateway pagamentoGateway;

    @Mock
    private ValidaCartao validaCartao;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        realizaPagamento = new RealizaPagamento(pagamentoGateway, validaCartao);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirRealizarPagamento() {
        // Arrange
        String token = "123csdf58498e798s7a6s8aaZZaaq@Rs";

        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        ConsomeLimiteResponseDTO responseDTO = new ConsomeLimiteResponseDTO(200, "OK", "1111 2222 3333 4444", 1200.0);

        Pagamento pagamentoEsperado = new Pagamento(null, pagamentoRequestDTO.cpf(),
                pagamentoRequestDTO.numero(), pagamentoRequestDTO.valor(),
                LocalDateTime.now(), "Compra realizada", "cartao_credito", "aprovado");

        when(validaCartao.execute(token, pagamentoRequestDTO.cpf(), pagamentoRequestDTO.numero(),
                pagamentoRequestDTO.data_validade(), pagamentoRequestDTO.cvv(), pagamentoRequestDTO.valor()))
                .thenReturn(responseDTO);

        when(pagamentoGateway.create(any(Pagamento.class))).thenReturn(pagamentoEsperado);

        // Act
        Pagamento resultado = realizaPagamento.execute(token, pagamentoRequestDTO);

        // Assert
        assertThat(resultado).isInstanceOf(Pagamento.class);
        assertThat(resultado.getStatus()).isEqualTo(pagamentoEsperado.getStatus());
    }

    @Test
    void deveLancarExcecaoQuandoErroDeLimite() {
        // Arrange
        String token = "123csdf58498e798s7a6s8aaZZaaq@Rs";

        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        ConsomeLimiteResponseDTO responseDTO = new ConsomeLimiteResponseDTO(402, "Recusada: sem limite", "1111 2222 3333 4444", 125.0);

        Pagamento pagamentoEsperado = new Pagamento(null, pagamentoRequestDTO.cpf(),
                pagamentoRequestDTO.numero(), pagamentoRequestDTO.valor(),
                LocalDateTime.now(), "Recusada: "+ responseDTO.message(), "cartao_credito", "recusado");

        when(validaCartao.execute(token, pagamentoRequestDTO.cpf(), pagamentoRequestDTO.numero(),
                pagamentoRequestDTO.data_validade(), pagamentoRequestDTO.cvv(), pagamentoRequestDTO.valor()))
                .thenReturn(responseDTO);

        when(pagamentoGateway.create(any(Pagamento.class))).thenReturn(pagamentoEsperado);

        // Assert
        assertThatThrownBy(() -> realizaPagamento.execute(token, pagamentoRequestDTO))
                .isInstanceOf(LimitErrorException.class)
                .hasMessage(responseDTO.message());
    }

    @Test
    void deveGerarExcecao()  {
        // Arrange
        String token = "123csdf58498e798s7a6s8aaZZaaq@Rs";

        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        ConsomeLimiteResponseDTO responseDTO = new ConsomeLimiteResponseDTO(401, "Recusada: sem limite", "1111 2222 3333 4444", 125.0);

        Pagamento pagamentoEsperado = new Pagamento(null, pagamentoRequestDTO.cpf(),
                pagamentoRequestDTO.numero(), pagamentoRequestDTO.valor(),
                LocalDateTime.now(), "Recusada: "+ responseDTO.message(), "cartao_credito", "recusado");

        when(validaCartao.execute(token, pagamentoRequestDTO.cpf(), pagamentoRequestDTO.numero(),
                pagamentoRequestDTO.data_validade(), pagamentoRequestDTO.cvv(), pagamentoRequestDTO.valor()))
                .thenReturn(responseDTO);

        when(pagamentoGateway.create(any(Pagamento.class))).thenReturn(pagamentoEsperado);

        // Assert
        assertThatThrownBy(() -> realizaPagamento.execute(token, pagamentoRequestDTO))
                .isInstanceOf(BussinessErrorException.class)
                .hasMessage(responseDTO.message());

    }

}
