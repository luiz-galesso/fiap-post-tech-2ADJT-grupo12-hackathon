package com.hackathon.fiap.mscliente.entity.cliente.gateway;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.repository.ClienteRepository;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class ClienteGateway {

    private ClienteRepository clienteRepository;

    public ClienteGateway(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente create(Cliente cliente) {
        return this.clienteRepository.save(cliente);
    }

    public Cliente update(Cliente cliente) {
        return this.clienteRepository.save(cliente);
    }

    public Optional<Cliente> findById(String cpf) {
        return this.clienteRepository.findById(cpf);
    }

    public void remove(String cpf) {
        clienteRepository.deleteById(cpf);
    }

}