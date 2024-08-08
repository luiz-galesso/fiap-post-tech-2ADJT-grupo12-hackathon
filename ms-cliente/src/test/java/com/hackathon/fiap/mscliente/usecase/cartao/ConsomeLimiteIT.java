package com.hackathon.fiap.mscliente.usecase.cartao;

import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mscliente.utils.CartaoHelper;
import com.hackathon.fiap.mscliente.utils.ClienteHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ConsomeLimiteIT {

    @Autowired
    ConsomeLimite consomeLimite;
    @Autowired
    CartaoGateway cartaoGateway;
    @Autowired
    ClienteGateway clienteGateway;

    @Test
    void devePermitirConsumirLimite() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());

        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));
        Double valor = 10D;

        ConsomeLimiteResponseDTO consomeLimiteResponseDTO = consomeLimite.execute(new ConsomeLimiteRequestDTO(cliente.getCpf()
                , cartaoRequestDTO.numero()
                , cartaoRequestDTO.data_validade()
                , cartaoRequestDTO.cvv()
                , valor));

        assertThat(consomeLimiteResponseDTO).isNotNull();
        assertThat(consomeLimiteResponseDTO.limiteRestante()).isNotEqualTo(cartaoRequestDTO.limite());
    }

    @Test
    void deveRetornarMensagem_QuandoConsumirLimite_LimiteIndisponivel() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());

        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));
        Double valor = cartaoRequestDTO.limite() + 10D;

        ConsomeLimiteResponseDTO consomeLimiteResponseDTO = consomeLimite.execute(new ConsomeLimiteRequestDTO(cliente.getCpf()
                , cartaoRequestDTO.numero()
                , cartaoRequestDTO.data_validade()
                , cartaoRequestDTO.cvv()
                , valor));

        assertThat(consomeLimiteResponseDTO).isNotNull();
        assertThat(consomeLimiteResponseDTO.message()).isEqualToIgnoringCase("Limite indisponível");

    }

}
