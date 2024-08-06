package com.hackathon.fiap.mspagamento.entity.gateway;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.hackathon.fiap.mspagamento.entity.pagamento.gateway.PagamentoGateway;
import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.repository.PagamentoRepository;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;


public class PagamentoGatewayTest {
    AutoCloseable openMocks;

    private PagamentoGateway gateway;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        gateway = new PagamentoGateway(pagamentoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCriarPagamento() {
        //arrange
        Pagamento pagamento = PagamentoHelper.gerarPagamento();
        when(gateway.create(any(Pagamento.class))).thenReturn(pagamento);

        //act
        var resultado = gateway.create(pagamento);

        //assert
        assertThat(resultado).isInstanceOf(Pagamento.class);
        assertThat(resultado.getId()).isEqualTo(pagamento.getId());
    }

    @Test
    void devePermitirBuscarPagamentosPorCpf() {
        //arrange
        String cpf = "12345678909";
        List<Pagamento> pagamentos = PagamentoHelper.criarLista(4);

        when(gateway.findByCpf(anyString())).thenReturn(pagamentos);

        //act
        var resultado = gateway.findByCpf(cpf);

        //assert
        assertThat(resultado.size()).isEqualTo(pagamentos.size());
       
    }

}
