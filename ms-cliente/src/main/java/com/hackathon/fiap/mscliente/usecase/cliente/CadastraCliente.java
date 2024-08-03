package com.hackathon.fiap.mscliente.usecase.cliente;

import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteResponseDTO;
import com.hackathon.fiap.mscliente.infrastructure.util.StringUtils;
import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CadastraCliente {

    private final ClienteGateway clienteGateway;

    public ClienteResponseDTO execute(ClienteRequestDTO clienteRequestDTO) {

        Cliente cliente = toEntity(clienteRequestDTO);
        Optional<Cliente> clienteOptional = clienteGateway.findById(cliente.getCpf());

        if (clienteOptional.isPresent()) {
            throw new BusinessErrorException("Já existe um cliente cadastrado com o cpf informado.");
        }

        var clienteSalvo = this.clienteGateway.create(cliente);
        return new ClienteResponseDTO(clienteSalvo.getCpf());
    }

    public Cliente toEntity(ClienteRequestDTO clienteRequestDTO)
    {
        return new Cliente(StringUtils.formataDados(clienteRequestDTO.cpf())
                , clienteRequestDTO.nome()
                , clienteRequestDTO.email()
                , StringUtils.formataDados(clienteRequestDTO.telefone())
                , clienteRequestDTO.rua()
                , clienteRequestDTO.cidade()
                , clienteRequestDTO.estado()
                , StringUtils.formataDados(clienteRequestDTO.cep())
                , clienteRequestDTO.pais()
        );
    }
}
