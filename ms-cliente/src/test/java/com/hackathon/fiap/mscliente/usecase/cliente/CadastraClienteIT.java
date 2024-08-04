package com.hackathon.fiap.mscliente.usecase.cliente;


import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteResponseDTO;
import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
import com.hackathon.fiap.mscliente.utils.ClienteHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class CadastraClienteIT {

    @Autowired
    CadastraCliente cadastrarCliente;
    @Autowired
    ClienteGateway clienteGateway;

    @Test
    void devePermitirCadastrarCliente() {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();

        var clienteRetornado = cadastrarCliente.execute(clienteRequestDTO);

        assertThat(clienteRetornado).isInstanceOf(ClienteResponseDTO.class);
        assertThat(clienteRetornado.id_cliente()).isNotNull();
    }

    @Test
    void deveGerarExcecao_QuandoCadastrarCliente_ClienteJaCadastrado() {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();
        var cliente = cadastrarCliente.execute(clienteRequestDTO);

        assertThatThrownBy(() -> cadastrarCliente.execute(clienteRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("Já existe um cliente cadastrado com o cpf informado.");
    }

}
