package com.hackathon.fiap.mspagamento.usecase.pagamento;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.feign.CartaoClient;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.usecase.cartao.ValidaCartao;
import com.hackathon.fiap.mspagamento.usecase.exception.BussinessErrorException;
import com.hackathon.fiap.mspagamento.usecase.exception.LimitErrorException;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class RealizaPagamentoIT {

    @Autowired
    RealizaPagamento realizaPagamento;

    @MockBean
    CartaoClient cartaoClient;


    @Test
    void devePermitirRealizarPagamento() {
        //arrange
        String token = "eyJraWQiOiJhZGoyIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJtcy1hdXRlbnRpY2FjYW8iLCJzdWIiOiJhZGoyIiwiUk9MRVMiOiJbUk9MRV9JTlRFR1JBVElPTl0iLCJleHAiOjE3MjI4MDMwNjd9.Xw86MiKloVckIqsfkMrgSKglRXOH1oEwxTLg7fuJUks";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        when(cartaoClient.consomeLimite(anyString(), any(ConsomeLimiteRequestDTO.class)))
                .thenReturn(PagamentoHelper.gerarConsomeLimiteResponseDTO());

        //act
        var resultado = realizaPagamento.execute(token, pagamentoRequestDTO);

        //assert
        assertThat(resultado).isInstanceOf(Pagamento.class);
    }

    @Test
    void deveLancarExcecaoQuandoErroDeLimite() {
        //arrange
        String token = "eyJraWQiOiJhZGoyIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJtcy1hdXRlbnRpY2FjYW8iLCJzdWIiOiJhZGoyIiwiUk9MRVMiOiJbUk9MRV9JTlRFR1JBVElPTl0iLCJleHAiOjE3MjI4MDMwNjd9.Xw86MiKloVckIqsfkMrgSKglRXOH1oEwxTLg7fuJUks";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        ConsomeLimiteResponseDTO consomeLimiteRequestDTO = PagamentoHelper.gerarConsomeLimiteResponseDTO_Error(402, "Recusada: não há limite disponível.", 100.0);

        when(cartaoClient.consomeLimite(anyString(), any(ConsomeLimiteRequestDTO.class)))
                .thenReturn(consomeLimiteRequestDTO);

        //assert
        assertThatThrownBy(() -> realizaPagamento.execute(token, pagamentoRequestDTO))
                .isInstanceOf(LimitErrorException.class)
                .hasMessageContaining(consomeLimiteRequestDTO.message());
    }

    @Test
    void deveGerarExcecao()  {
        //arrange
        String token = "eyJraWQiOiJhZGoyIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJtcy1hdXRlbnRpY2FjYW8iLCJzdWIiOiJhZGoyIiwiUk9MRVMiOiJbUk9MRV9JTlRFR1JBVElPTl0iLCJleHAiOjE3MjI4MDMwNjd9.Xw86MiKloVckIqsfkMrgSKglRXOH1oEwxTLg7fuJUks";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        ConsomeLimiteResponseDTO consomeLimiteRequestDTO = PagamentoHelper.gerarConsomeLimiteResponseDTO_Error(401, "Erro de comunicação.", 0.0);

        when(cartaoClient.consomeLimite(anyString(), any(ConsomeLimiteRequestDTO.class)))
                .thenReturn(consomeLimiteRequestDTO);

        //assert
        assertThatThrownBy(() -> realizaPagamento.execute(token, pagamentoRequestDTO))
                .isInstanceOf(BussinessErrorException.class)
                .hasMessageContaining(consomeLimiteRequestDTO.message());
    }

}
