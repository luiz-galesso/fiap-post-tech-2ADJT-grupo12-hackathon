package com.hackathon.fiap.mscliente.entity.cartao.gateway;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.infrastructure.cartao.repository.CartaoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public Optional<Cartao> findById(Integer idEndereco) {
        return this.cartaoRepository.findById(idEndereco);
    }

    public List<Cartao> findByUsuario(Cliente cliente) {
        return this.cartaoRepository.findByUsuario(cliente);
    }
    public void remove(Integer id) {
        cartaoRepository.deleteById(id);
    }

}