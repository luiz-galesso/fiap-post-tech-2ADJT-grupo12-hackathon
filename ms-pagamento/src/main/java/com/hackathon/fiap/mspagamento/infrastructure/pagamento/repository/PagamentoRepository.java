package com.hackathon.fiap.mspagamento.infrastructure.pagamento.repository;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, String> {

    List<Pagamento> findByUsuario(Pagamento pagamento);

}
