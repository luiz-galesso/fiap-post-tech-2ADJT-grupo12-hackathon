package com.hackathon.fiap.mscliente.usecase.cliente;

import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteResponseDTO;
import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
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

class CadastraClienteTest {

    @Mock
    ClienteGateway clienteGateway;
    CadastraCliente cadastrarCliente;
    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        cadastrarCliente = new CadastraCliente(clienteGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCadastrarCliente() {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();
        Cliente cliente = new Cliente(clienteRequestDTO.cpf()
                , clienteRequestDTO.nome()
                , clienteRequestDTO.email()
                , clienteRequestDTO.telefone()
                , clienteRequestDTO.rua()
                , clienteRequestDTO.cidade()
                , clienteRequestDTO.estado()
                , clienteRequestDTO.cep()
                , clienteRequestDTO.pais());
        when(clienteGateway.create(any(Cliente.class))).thenReturn(cliente);

        var clienteRetornado = cadastrarCliente.execute(clienteRequestDTO);

        assertThat(clienteRetornado).isInstanceOf(ClienteResponseDTO.class);
        assertThat(clienteRetornado.id_cliente()).isNotNull();
        verify(clienteGateway, times(1)).create(any(Cliente.class));
    }

    @Test
    void deveGerarExcecao_QuandoCadastrarCliente_ClienteJaCadastrado() {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();
        Cliente cliente = new Cliente(clienteRequestDTO.cpf()
                , clienteRequestDTO.nome()
                , clienteRequestDTO.email()
                , clienteRequestDTO.telefone()
                , clienteRequestDTO.rua()
                , clienteRequestDTO.cidade()
                , clienteRequestDTO.estado()
                , clienteRequestDTO.cep()
                , clienteRequestDTO.pais());
        clienteGateway.create(cliente);
        when(clienteGateway.findById(any(String.class))).thenReturn(Optional.of(cliente));
        when(clienteGateway.create(any(Cliente.class))).thenReturn(cliente);

        assertThatThrownBy(() -> cadastrarCliente.execute(clienteRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("Já existe um cliente cadastrado com o cpf informado.");
        verify(clienteGateway, times(1)).create(any(Cliente.class));
    }
}
