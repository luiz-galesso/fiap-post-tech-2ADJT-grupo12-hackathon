package com.hackathon.fiap.mspagamento.usecase.pagamento;

import com.hackathon.fiap.mspagamento.entity.pagamento.gateway.PagamentoGateway;
import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoResponseDTO;
import com.hackathon.fiap.mspagamento.usecase.cartao.ValidaCartao;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ObtemPagamentosPorClienteTest {

    AutoCloseable autoCloseable;

    private ObtemPagamentosPorCliente obtemPagamentosPorCliente;

    @Mock
    private PagamentoGateway pagamentoGateway;

    @Mock
    private ValidaCartao validaCartao;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        obtemPagamentosPorCliente = new ObtemPagamentosPorCliente(pagamentoGateway, validaCartao);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void deveObterPagamentosPorCliente() {

        //arrange
        String cpf = "12345678909";

        List<Pagamento> pagamentos = PagamentoHelper.criarLista(3);

        when(pagamentoGateway.findByCpf(anyString())).thenReturn(pagamentos);

        //act
        var resultado = obtemPagamentosPorCliente.execute(cpf);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(3);

    }


}
