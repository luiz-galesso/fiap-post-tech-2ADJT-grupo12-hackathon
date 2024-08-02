package com.hackathon.fiap.mspagamento.usecase.cartao;


import com.hackathon.fiap.mspagamento.infrastructure.exception.ErrorDefaultResponse;
import com.hackathon.fiap.mspagamento.infrastructure.feign.CartaoClient;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.feign.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mspagamento.usecase.exception.BussinessErrorException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ValidaCartao {

    private final CartaoClient cartaoClient;

    public ConsomeLimiteResponseDTO execute(String token, String cpf, String numeroCartao, String validadeCartao, String cvv, Double valor) {
        ConsomeLimiteRequestDTO request = new ConsomeLimiteRequestDTO(cpf, numeroCartao,validadeCartao, cvv, valor);
        try {
            ConsomeLimiteResponseDTO response = this.cartaoClient.consomeLimite(token, request);
            return response;
        }
        catch( FeignException e){
            throw new BussinessErrorException("Erro de comunicação com api de cartões. "+e.getMessage());
        }
    }

}
