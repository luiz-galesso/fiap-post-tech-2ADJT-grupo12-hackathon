package com.hackathon.fiap.mspagamento.entity.gateway;

import com.hackathon.fiap.mspagamento.entity.pagamento.gateway.PagamentoGateway;
import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class PagamentoGatewayIT {

    @Autowired
    PagamentoGateway gateway;

    @Test
    void devePermitirCriarPagamento() {
        //arrange
        Pagamento pagamento = PagamentoHelper.gerarPagamento();

        //act
        var resultado = gateway.create(pagamento);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado).isInstanceOf(Pagamento.class);
    }

    @Test
    void devePermitirBuscarPagamentosPorCpf() {
        //arrange
        String cpf = "12345678909";

        //act
        var resultado = gateway.findByCpf(cpf);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isInstanceOf(List.class);
    }

}
