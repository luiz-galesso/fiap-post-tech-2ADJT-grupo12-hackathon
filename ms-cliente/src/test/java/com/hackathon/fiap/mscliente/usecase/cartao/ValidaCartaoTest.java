package com.hackathon.fiap.mscliente.usecase.cartao;

import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ValidaCartaoResponseDTO;
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


class ValidaCartaoTest {
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
    void devePermitirValidarCartao() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        Cartao cartao = new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente);

        clienteGateway.create(cliente);

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.findById(any(String.class))).thenReturn(Optional.of(cartao));

        ValidaCartaoResponseDTO validaCartaoResponseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), cartaoRequestDTO.data_validade(), cartaoRequestDTO.cvv());
        assertThat(validaCartaoResponseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(validaCartaoResponseDTO).isNotNull();
        assertThat(validaCartaoResponseDTO.message()).isEqualToIgnoringCase("Cartão validado");
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CartaoNaoExiste() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());

        clienteGateway.create(cliente);

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));

        ValidaCartaoResponseDTO validaCartaoResponseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), cartaoRequestDTO.data_validade(), cartaoRequestDTO.cvv());
        assertThat(validaCartaoResponseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(validaCartaoResponseDTO).isNotNull();
        assertThat(validaCartaoResponseDTO.message()).isEqualToIgnoringCase("Não existe cartão com o número informado");
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CartaoNaoPertenceCliente() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        Cartao cartao = new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente);

        clienteGateway.create(cliente);

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.findById(any(String.class))).thenReturn(Optional.of(cartao));

        ValidaCartaoResponseDTO validaCartaoResponseDTO = validaCartao.execute(cartaoRequestDTO.numero(), "11122233344", cartaoRequestDTO.data_validade(), cartaoRequestDTO.cvv());
        assertThat(validaCartaoResponseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(validaCartaoResponseDTO).isNotNull();
        assertThat(validaCartaoResponseDTO.message()).isEqualToIgnoringCase("Cartão não pertence ao cliente.");
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_DataValidadeDiferente() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        Cartao cartao = new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente);

        clienteGateway.create(cliente);

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.findById(any(String.class))).thenReturn(Optional.of(cartao));

        ValidaCartaoResponseDTO validaCartaoResponseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), "01-29", cartaoRequestDTO.cvv());
        assertThat(validaCartaoResponseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(validaCartaoResponseDTO).isNotNull();
        assertThat(validaCartaoResponseDTO.message()).isEqualToIgnoringCase("Data de validade informada não é igual a data de validade do cartão");
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CartaoVencido() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        Cartao cartao = new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), "01-22", Integer.parseInt(cartaoRequestDTO.cvv()), cliente);

        clienteGateway.create(cliente);

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.findById(any(String.class))).thenReturn(Optional.of(cartao));

        ValidaCartaoResponseDTO validaCartaoResponseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), "01-22", cartaoRequestDTO.cvv());
        assertThat(validaCartaoResponseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(validaCartaoResponseDTO).isNotNull();
        assertThat(validaCartaoResponseDTO.message()).isEqualToIgnoringCase("Cartão vencido");
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CvvInvalido() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        Cartao cartao = new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente);

        clienteGateway.create(cliente);

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.findById(any(String.class))).thenReturn(Optional.of(cartao));

        ValidaCartaoResponseDTO validaCartaoResponseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), cartaoRequestDTO.data_validade(), "001");
        assertThat(validaCartaoResponseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(validaCartaoResponseDTO).isNotNull();
        assertThat(validaCartaoResponseDTO.message()).isEqualToIgnoringCase("CVV inválido");
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CvvFormatoInvalido() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        Cartao cartao = new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente);

        clienteGateway.create(cliente);

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.findById(any(String.class))).thenReturn(Optional.of(cartao));

        ValidaCartaoResponseDTO validaCartaoResponseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), cartaoRequestDTO.data_validade(), "A201");
        assertThat(validaCartaoResponseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(validaCartaoResponseDTO).isNotNull();
        assertThat(validaCartaoResponseDTO.message()).isEqualToIgnoringCase("CVV em formato inválido");
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }


}
