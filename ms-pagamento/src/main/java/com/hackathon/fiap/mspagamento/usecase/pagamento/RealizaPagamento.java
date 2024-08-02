package com.hackathon.fiap.mspagamento.usecase.pagamento;

import com.hackathon.fiap.mspagamento.entity.pagamento.gateway.PagamentoGateway;
import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoResponseDTO;
import com.hackathon.fiap.mspagamento.usecase.cartao.ValidaCartao;
import com.hackathon.fiap.mspagamento.usecase.exception.BussinessErrorException;
import com.hackathon.fiap.mspagamento.usecase.exception.LimitErrorException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RealizaPagamento {

    private final PagamentoGateway pagamentoGateway;
    private final ValidaCartao validaCartao;

    public Pagamento execute(String token, PagamentoRequestDTO pagamentoRequestDTO) {
        ConsomeLimiteResponseDTO response = validaCartao.execute(token, pagamentoRequestDTO.cpf(),pagamentoRequestDTO.numero(),pagamentoRequestDTO.data_validade(),pagamentoRequestDTO.cvv(), pagamentoRequestDTO.valor());
        Pagamento pagamento = new Pagamento();
        if (response.codigoErro() != 200) {
            pagamento = new Pagamento(null, pagamentoRequestDTO.cpf()
                    , pagamentoRequestDTO.numero(), pagamentoRequestDTO.valor()
                    , LocalDateTime.now(), "Recusada: "+ response.message(), "cartao_credito", "reprovado");
            pagamento = pagamentoGateway.create(pagamento);
            if (response.codigoErro() == 402) {
                throw new LimitErrorException(response.message());
            }
            throw new BussinessErrorException(response.message());
        }

        pagamento = new Pagamento(null, pagamentoRequestDTO.cpf()
                , pagamentoRequestDTO.numero(), pagamentoRequestDTO.valor()
                , LocalDateTime.now(), "Compra realizada", "cartao_credito", "aprovado");
        pagamentoGateway.create(pagamento);

        return pagamento;
    }
}
