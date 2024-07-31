package com.hackathon.fiap.mspagamento.entity.pagamento.gateway;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.repository.PagamentoRepository;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class PagamentoGateway {

    private PagamentoRepository pagamentoRepository;

    public PagamentoGateway(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public Pagamento create(Pagamento pagamento) {
        return this.pagamentoRepository.save(pagamento);
    }

    public Pagamento update(Pagamento pagamento) {
        return this.pagamentoRepository.save(pagamento);
    }

    public Optional<Pagamento> findById(String id) {
        return this.pagamentoRepository.findById(id);
    }

    public void remove(String id) {
        pagamentoRepository.deleteById(id);
    }

}