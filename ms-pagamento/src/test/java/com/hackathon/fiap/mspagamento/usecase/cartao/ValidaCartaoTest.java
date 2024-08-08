package com.hackathon.fiap.mspagamento.usecase.cartao;

import com.hackathon.fiap.mspagamento.infrastructure.feign.CartaoClient;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.usecase.exception.BussinessErrorException;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ValidaCartaoTest {

    AutoCloseable autoCloseable;

    private ValidaCartao validaCartao;

    @Mock
    private CartaoClient cartaoClient;


    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        validaCartao = new ValidaCartao(cartaoClient);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void deveExecutar() {
        //arrange
        String token = "hjasdsd78687136qA!@!2232sa5d6s";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        ConsomeLimiteResponseDTO response =
                new ConsomeLimiteResponseDTO(200, "OK", pagamentoRequestDTO.numero(), pagamentoRequestDTO.valor());

        when(cartaoClient.consomeLimite(anyString(), any(ConsomeLimiteRequestDTO.class))).thenReturn(response);

        //act
        var resultado = validaCartao.execute(token, pagamentoRequestDTO.cpf(), pagamentoRequestDTO.numero(), pagamentoRequestDTO.data_validade(), pagamentoRequestDTO.cvv(), pagamentoRequestDTO.valor());

        //assert
        assertThat(resultado).isInstanceOf(ConsomeLimiteResponseDTO.class);
        assertThat(resultado.codigoErro()).isEqualTo(200);
    }

    @Test
    void deveGerarExecao_QuandoFeignException() {

        //arrange
        String token = "hjasdsd78687136qA!@!2232sa5d6s";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        when(cartaoClient.consomeLimite(anyString(), any(ConsomeLimiteRequestDTO.class)))
                .thenThrow(FeignException.class);

        // act & assert
        assertThatThrownBy(() ->
                validaCartao.execute(token, pagamentoRequestDTO.cpf(), pagamentoRequestDTO.numero(), pagamentoRequestDTO.data_validade(), pagamentoRequestDTO.cvv(), pagamentoRequestDTO.valor()))
                .isInstanceOf(BussinessErrorException.class)
                .hasMessageContaining("Erro de comunicação com api de cartões.");
    }

}
