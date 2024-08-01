package com.hackathon.fiap.mscliente.entity.cartao.gateway;

import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.repository.CartaoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CartaoGateway {

    private final CartaoRepository cartaoRepository;

    public CartaoGateway(CartaoRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
    }

    public Cartao create(Cartao cartao) {
        return this.cartaoRepository.save(cartao);
    }

    public Cartao update(Cartao cartao) {
        return this.cartaoRepository.save(cartao);
    }

    public Optional<Cartao> findById(String numeroCartao) {
        return this.cartaoRepository.findById(numeroCartao);
    }

    public long countByCliente(Cliente cliente) { return this.cartaoRepository.countByCliente(cliente);}
}