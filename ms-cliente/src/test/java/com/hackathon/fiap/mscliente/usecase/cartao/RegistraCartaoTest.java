package com.hackathon.fiap.mscliente.usecase.cartao;

import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
import com.hackathon.fiap.mscliente.utils.CartaoHelper;
import com.hackathon.fiap.mscliente.utils.ClienteHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistraCartaoTest {
    @Mock
    CartaoGateway cartaoGateway;
    @Mock
    ClienteGateway clienteGateway;
    RegistraCartao registraCartao;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        registraCartao = new RegistraCartao(cartaoGateway, clienteGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirRegistrarCartao() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        Cartao cartao = new Cartao();

        clienteGateway.create(cliente);

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.create(any(Cartao.class))).thenReturn(cartao);

        registraCartao.execute(cartaoRequestDTO);
        var retorno = cartaoGateway.findById(cartaoRequestDTO.numero());

        assertThat(retorno).isInstanceOf(Optional.class);
        assertThat(retorno).isNotNull();
        verify(clienteGateway, times(1)).findById(any(String.class));
        verify(cartaoGateway, times(1)).create(any(Cartao.class));
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_CartaoJaCadastrado() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        Cartao cartao = new Cartao();

        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(),cartaoRequestDTO.limite(),cartaoRequestDTO.data_validade(),Integer.parseInt(cartaoRequestDTO.cvv()),cliente));

        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(cartaoGateway.findById(any(String.class))).thenReturn(Optional.of(cartao));

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("Já existe cartão com o número informado.");
        verify(cartaoGateway, times(1)).findById(any(String.class));
    }

    

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_LimiteNegativoOuZero() {
        Cliente cliente = ClienteHelper.gerarCliente();
        clienteGateway.create(cliente);
        CartaoRequestDTO cartaoRequestDTO = new CartaoRequestDTO(cliente.getCpf(), 0D, "1234 5678 9012 3456", "12/24", "123");

        clienteGateway.create(cliente);
        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("Limite não pode ser negativo ou zero.");
        verify(clienteGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_DataValidadeInvalida() {
        Cliente cliente = ClienteHelper.gerarCliente();
        clienteGateway.create(cliente);
        CartaoRequestDTO cartaoRequestDTO = new CartaoRequestDTO(cliente.getCpf(), 1000.00, "6421 5031 9012 5471", "12/A2024", "123");

        clienteGateway.create(cliente);
        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("Data de validade em formato inválido");
        verify(clienteGateway, times(1)).findById(any(String.class));
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_ClienteNaoCadastrado() {
        Cliente cliente = ClienteHelper.gerarCliente();
        clienteGateway.create(cliente);
        CartaoRequestDTO cartaoRequestDTO = new CartaoRequestDTO(cliente.getCpf(), 1500.00, "8471 5678 9012 5148", "12/24", "A123");

        clienteGateway.create(cliente);
        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("CVV em formato inválido");
        verify(clienteGateway, times(1)).findById(any(String.class));
    }

}
