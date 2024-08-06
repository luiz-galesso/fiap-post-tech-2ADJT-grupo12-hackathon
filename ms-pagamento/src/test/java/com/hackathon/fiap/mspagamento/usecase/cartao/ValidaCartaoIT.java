package com.hackathon.fiap.mspagamento.usecase.cartao;

import com.hackathon.fiap.mspagamento.infrastructure.feign.CartaoClient;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoResponseDTO;
import com.hackathon.fiap.mspagamento.usecase.exception.BussinessErrorException;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class ValidaCartaoIT {

    @Autowired
    ValidaCartao validaCartao;

    @MockBean
    private CartaoClient cartaoClient;

    @Test
    void deveExecutar() {
        //arrange
        String token = "eyJraWQiOiJhZGoyIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJtcy1hdXRlbnRpY2FjYW8iLCJzdWIiOiJhZGoyIiwiUk9MRVMiOiJbUk9MRV9JTlRFR1JBVElPTl0iLCJleHAiOjE3MjI4MDc2NjZ9.9amC42IS9f0jAD7Fpsc09tvl1c8EP8jipEvkw9sEATE";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        PagamentoResponseDTO responseDTO = PagamentoHelper.gerarPagamentoResponseDTO();

        when(cartaoClient.consomeLimite(anyString(), any(ConsomeLimiteRequestDTO.class)))
                .thenReturn(PagamentoHelper.gerarConsomeLimiteResponseDTO());

        //act
        var resultado = validaCartao.execute(
                token,
                pagamentoRequestDTO.cpf(),
                pagamentoRequestDTO.numero(),
                pagamentoRequestDTO.data_validade(),
                pagamentoRequestDTO.cvv(),
                pagamentoRequestDTO.valor()
        );

        //assert
        assertThat(resultado).isNotNull();
    }

    @Test
    void deveGerarExecao_QuandoFeignException() {
        //arrange
        String token = "eyJraWQiOiJhZGoyIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJtcy1hdXRlbnRpY2FjYW8iLCJzdWIiOiJhZGoyIiwiUk9MRVMiOiJbUk9MRV9JTlRFR1JBVElPTl0iLCJleHAiOjE3MjI4MDMwNjd9.Xw86MiKloVckIqsfkMrgSKglRXOH1oEwxTLg7fuJUks";
        PagamentoRequestDTO pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();

        when(cartaoClient.consomeLimite(anyString(), any(ConsomeLimiteRequestDTO.class)))
                .thenThrow(FeignException.class);

        BussinessErrorException thrownException = assertThrows(BussinessErrorException.class, () -> validaCartao.execute(
                token,
                pagamentoRequestDTO.cpf(),
                pagamentoRequestDTO.numero(),
                pagamentoRequestDTO.data_validade(),
                pagamentoRequestDTO.cvv(),
                pagamentoRequestDTO.valor()));

        assertTrue(thrownException.getMessage().contains("Erro de comunicação com api de cartões."));
    }
}
