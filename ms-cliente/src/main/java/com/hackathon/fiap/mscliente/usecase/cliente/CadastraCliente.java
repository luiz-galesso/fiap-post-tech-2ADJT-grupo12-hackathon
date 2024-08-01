package com.hackathon.fiap.mscliente.usecase.cliente;

import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteResponseDTO;
import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CadastraCliente {

    private final ClienteGateway clienteGateway;

    public ClienteResponseDTO execute(ClienteRequestDTO clienteRequestDTO) {

        Optional<Cliente> clienteOptional = clienteGateway.findById(clienteRequestDTO.cpf());

        if (clienteOptional.isPresent()) {
            throw new BusinessErrorException("Já existe um cliente cadastrado com o cpf informado.");
        }

        var cliente = this.clienteGateway.create(toEntity(clienteRequestDTO));
        return new ClienteResponseDTO(cliente.getCpf());
    }

    public Cliente toEntity(ClienteRequestDTO clienteRequestDTO)
    {
        return new Cliente(clienteRequestDTO.cpf()
                , clienteRequestDTO.nome()
                , clienteRequestDTO.email()
                , clienteRequestDTO.telefone()
                , clienteRequestDTO.rua()
                , clienteRequestDTO.cidade()
                , clienteRequestDTO.estado()
                , clienteRequestDTO.cep()
                , clienteRequestDTO.pais()
        );
    }

    public ClienteRequestDTO toClienteRequestDTO(Cliente cliente) {

        return new ClienteRequestDTO(cliente.getCpf()
                , cliente.getNome()
                , cliente.getEmail()
                , cliente.getTelefone()
                , cliente.getRua()
                , cliente.getCidade()
                , cliente.getEstado()
                , cliente.getCep()
                , cliente.getPais()
        );
    }
}
