package com.hackathon.fiap.mscliente.usecase.cartao;

import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ValidaCartaoRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ValidaCartaoResponseDTO;
import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConsomeLimite {

    private final CartaoGateway cartaoGateway;
    private final ValidaCartao validaCartao;

    public ConsomeLimiteResponseDTO execute(ConsomeLimiteRequestDTO consomeLimiteRequestDTO) {
        ValidaCartaoResponseDTO validaCartaoResponseDTO = validaCartao.execute(consomeLimiteRequestDTO.numero().replaceAll(" ", ""), consomeLimiteRequestDTO.cpf(), consomeLimiteRequestDTO.data_validade(), consomeLimiteRequestDTO.cvv());
        if (validaCartaoResponseDTO.codigoErro() != 200) {
            return new ConsomeLimiteResponseDTO(validaCartaoResponseDTO.codigoErro(),validaCartaoResponseDTO.message(),null, null);
        }
        Cartao cartao = cartaoGateway.findById(consomeLimiteRequestDTO.numero().replaceAll(" ", "")).orElseThrow();
        if (cartao.getLimite() - consomeLimiteRequestDTO.valor() < 0) {
            return new ConsomeLimiteResponseDTO(402,"Limite indisponível",cartao.getNumeroCartao(),cartao.getLimite());
        }
        cartao.setLimite(cartao.getLimite() - consomeLimiteRequestDTO.valor());
        cartaoGateway.update(cartao);

        return new ConsomeLimiteResponseDTO(200,"Transação realizada",cartao.getNumeroCartao(),cartao.getLimite());
    }
}

