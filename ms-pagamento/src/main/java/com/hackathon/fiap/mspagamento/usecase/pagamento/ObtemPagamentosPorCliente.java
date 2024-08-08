package com.hackathon.fiap.mspagamento.usecase.pagamento;

import com.hackathon.fiap.mspagamento.entity.pagamento.gateway.PagamentoGateway;
import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoResponseDTO;
import com.hackathon.fiap.mspagamento.usecase.cartao.ValidaCartao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ObtemPagamentosPorCliente {

    private final PagamentoGateway pagamentoGateway;
    private final ValidaCartao validaCartao;

    public List<PagamentoResponseDTO> execute(String cpf) {
        List<PagamentoResponseDTO> pagamentoResponseDTOList = new ArrayList<>();
        List<Pagamento> pagamentoList = pagamentoGateway.findByCpf(cpf);

        pagamentoList.stream().forEach(pagamento -> {
           pagamentoResponseDTOList.add(new PagamentoResponseDTO(pagamento.getValor(),pagamento.getDescricao(),pagamento.getMetodoPagamento(),pagamento.getStatus()));
        });

        return pagamentoResponseDTOList;
    }
}
