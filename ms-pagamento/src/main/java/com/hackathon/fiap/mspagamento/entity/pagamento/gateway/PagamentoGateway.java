package com.hackathon.fiap.mspagamento.entity.pagamento.gateway;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.repository.PagamentoRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PagamentoGateway {

    private PagamentoRepository pagamentoRepository;

    public PagamentoGateway(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public Pagamento create(Pagamento pagamento) {
        return this.pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> findByCpf(String cpf)  {
        return this.pagamentoRepository.findByCpf(cpf);
    }

}