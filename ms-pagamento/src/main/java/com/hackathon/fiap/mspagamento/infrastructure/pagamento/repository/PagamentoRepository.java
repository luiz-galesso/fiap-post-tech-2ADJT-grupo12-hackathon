package com.hackathon.fiap.mspagamento.infrastructure.pagamento.repository;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    List<Pagamento> findByCpf(String cpf);

}
