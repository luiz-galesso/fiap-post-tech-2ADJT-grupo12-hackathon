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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ConsomeLimiteTest {
    @Mock
    CartaoGateway cartaoGateway;
    @Mock
    ClienteGateway clienteGateway;
    ValidaCartao validaCartao;
    ConsomeLimite consomeLimite;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        validaCartao = new ValidaCartao(cartaoGateway);
        consomeLimite = new ConsomeLimite(cartaoGateway, validaCartao);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirConsumirLimite() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());

        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));
        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        Double valor = 10D;

        ConsomeLimiteResponseDTO consomeLimiteResponseDTO = consomeLimite.execute(new ConsomeLimiteRequestDTO(cliente.getCpf()
                , cartaoRequestDTO.numero()
                , cartaoRequestDTO.data_validade()
                , cartaoRequestDTO.cvv()
                , valor));

        assertThat(consomeLimiteResponseDTO).isNotNull();
        assertThat(consomeLimiteResponseDTO.limiteRestante()).isNotEqualTo(cartaoRequestDTO.limite());
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveRetornarMensagem_QuandoConsumirLimite_LimiteIndisponivel() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());

        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));
        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        Double valor = cartaoRequestDTO.limite() + 10D;

        ConsomeLimiteResponseDTO consomeLimiteResponseDTO = consomeLimite.execute(new ConsomeLimiteRequestDTO(cliente.getCpf()
                , cartaoRequestDTO.numero()
                , cartaoRequestDTO.data_validade()
                , cartaoRequestDTO.cvv()
                , valor));

        assertThat(consomeLimiteResponseDTO).isNotNull();
        //assertThat(consomeLimiteResponseDTO.message()).isEqualToIgnoringCase("Limite indisponível");
        verify(cartaoGateway, times(1)).findById(any(String.class));

    }

}
