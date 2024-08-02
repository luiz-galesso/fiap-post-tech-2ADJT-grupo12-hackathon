package com.hackathon.fiap.mscliente.usecase.cartao;

import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
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
public class ValidaCartao {

    private final CartaoGateway cartaoGateway;

    public ValidaCartaoResponseDTO execute(String numeroCartao, String cpf, String dataValidade, String cvv) {
        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yy");
        DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MM");
        LocalDate data = LocalDate.now();

        Optional<Cartao> cartaoOptional = cartaoGateway.findById(numeroCartao.replaceAll(" ", ""));
        if (cartaoOptional.isEmpty()) {
            return new ValidaCartaoResponseDTO(500, "Não existe cartão com o número informado");
        }
        if (!cpf.equals(cartaoOptional.get().getCliente().getCpf())) {
            return new ValidaCartaoResponseDTO(500,"Cartão não pertence ao cliente.");
        }
        if (!dataValidade.equals(cartaoOptional.get().getDataValidade())) {
            return new ValidaCartaoResponseDTO(500,"Data de validade informada não é igual a data de validade do cartão");
        }
        if (Integer.parseInt(cartaoOptional.get().getDataValidade().substring(3,5)) < Integer.parseInt(data.format(formatterYear))) {
            return new ValidaCartaoResponseDTO(500,"Cartão vencido");
        }
        if (Integer.parseInt(cartaoOptional.get().getDataValidade().substring(3,5)) == Integer.parseInt(data.format(formatterYear))) {
            if (Integer.parseInt(cartaoOptional.get().getDataValidade().substring(0,2)) < Integer.parseInt(data.format(formatterMonth))) {
                return new ValidaCartaoResponseDTO(500,"Cartão vencido");
            }
        }
        try {
            if (Integer.parseInt(cvv) != cartaoOptional.get().getCvv()) {
                return new ValidaCartaoResponseDTO(500,"CVV inválido");
            }
        } catch (NumberFormatException ex) {
            return new ValidaCartaoResponseDTO(500,"CVV em formato inválido");
        }
        return new ValidaCartaoResponseDTO(200,"Cartão validado");
    }
}

