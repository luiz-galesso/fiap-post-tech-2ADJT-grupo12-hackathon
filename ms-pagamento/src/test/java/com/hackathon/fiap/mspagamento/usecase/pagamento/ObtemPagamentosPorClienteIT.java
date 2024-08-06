package com.hackathon.fiap.mspagamento.usecase.pagamento;

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
public class ObtemPagamentosPorClienteIT {

    @Autowired
    ObtemPagamentosPorCliente obtemPagamentosPorCliente;

    @Test
    void deveObterPagamentosPorCliente() {
        //act
        var resultado = obtemPagamentosPorCliente.execute("12345678909");

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isInstanceOf(List.class);
    }

}
