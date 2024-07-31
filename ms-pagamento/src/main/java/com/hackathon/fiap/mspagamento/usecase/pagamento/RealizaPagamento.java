package com.hackathon.fiap.mspagamento.usecase.pagamento;

import com.hackathon.fiap.mspagamento.entity.pagamento.gateway.PagamentoGateway;
import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RealizaPagamento {

    private final PagamentoGateway pagamentoGateway;

    public Pagamento execute(PagamentoRequestDTO pagamentoRequestDTO) {

        return null;
    }
}
