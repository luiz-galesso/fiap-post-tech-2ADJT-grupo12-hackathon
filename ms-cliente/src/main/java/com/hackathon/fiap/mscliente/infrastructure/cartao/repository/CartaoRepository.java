package com.hackathon.fiap.mscliente.infrastructure.cartao.repository;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Integer> {

    List<Cartao> findByUsuario(Cliente cliente);

}
