package com.hackathon.fiap.mspagamento.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoResponseDTO;

public class PagamentoHelper {

    public static Pagamento gerarPagamento() {
        return new Pagamento(
             UUID.randomUUID(),
             "12345678909",
             "1111 2222 3333 4444",
             255.00,
             LocalDateTime.now(),
            "Xiaomi Mi Band 8",
            "cartao-credito",
            "aprovado"
        );
    }

    public static List<Pagamento> criarLista(int quantidade) {

        List<Pagamento> pagamentos = new ArrayList<>();

        for(int i = 0; i < quantidade; i++) {
            pagamentos.add(gerarPagamento());
        }

        return pagamentos;
    }

    public static List<PagamentoResponseDTO> gerarLista(List<Pagamento> pagamentoList) {
        List<PagamentoResponseDTO> pagamentoResponseDTOList = new ArrayList<>();

        pagamentoList.forEach(pagamento -> {
            pagamentoResponseDTOList.add(new PagamentoResponseDTO(pagamento.getValor(),pagamento.getDescricao(),pagamento.getMetodoPagamento(),pagamento.getStatus()));
        });
        return pagamentoResponseDTOList;
    }

    public static PagamentoRequestDTO gerarPagamentoRequestDTO () {
        return new PagamentoRequestDTO("12345678909", "1111 2222 3333 4444", "02/33", "123", 255.00);
    }

    public static PagamentoResponseDTO gerarPagamentoResponseDTO() {
        PagamentoRequestDTO requestDTO = gerarPagamentoRequestDTO();

        return new PagamentoResponseDTO(
            requestDTO.valor(),
            "Descrição",
            "cartao-credito",
            "OK"
        );

    }

    public static ConsomeLimiteResponseDTO gerarConsomeLimiteResponseDTO() {
        PagamentoRequestDTO requestDTO = gerarPagamentoRequestDTO();

        return new ConsomeLimiteResponseDTO(
            200,
                "OK",
                requestDTO.numero(),
                1200.0
        );

    }

    public static ConsomeLimiteResponseDTO gerarConsomeLimiteResponseDTO_Error(Integer codErro, String message, Double limit) {
        PagamentoRequestDTO requestDTO = gerarPagamentoRequestDTO();

        return new ConsomeLimiteResponseDTO(
                codErro,
                message,
                requestDTO.numero(),
                limit
        );

    }

}
