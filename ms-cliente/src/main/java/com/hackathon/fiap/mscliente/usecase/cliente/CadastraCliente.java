package com.hackathon.fiap.mscliente.usecase.cliente;

import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CadastraCliente {

    private final ClienteGateway clienteGateway;

    public Cliente execute(ClienteRequestDTO clienteRequestDTO) {

        return null;
    }
}
