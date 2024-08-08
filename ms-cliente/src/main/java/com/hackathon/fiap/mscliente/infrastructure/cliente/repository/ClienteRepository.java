package com.hackathon.fiap.mscliente.infrastructure.cliente.repository;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

}
