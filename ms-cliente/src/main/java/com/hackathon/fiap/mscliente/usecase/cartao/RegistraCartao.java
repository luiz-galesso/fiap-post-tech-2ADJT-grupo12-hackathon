package com.hackathon.fiap.mscliente.usecase.cartao;

import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
import com.hackathon.fiap.mscliente.usecase.exception.ForbiddenErrorException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistraCartao {

    private final CartaoGateway cartaoGateway;
    private final ClienteGateway clienteGateway;

    public void execute(CartaoRequestDTO cartaoRequestDTO) {

        Integer cvv;
        Integer mes;
        Integer ano;

        Optional<Cartao> cartaoOptional = cartaoGateway.findById(cartaoRequestDTO.numero().replaceAll(" ", ""));
        if (cartaoOptional.isPresent()) {
            throw new BusinessErrorException("Já existe cartão com o número informado.");
        }
        Optional<Cliente> clienteOptional = clienteGateway.findById(cartaoRequestDTO.cpf());
        if (clienteOptional.isEmpty()) {
            throw new BusinessErrorException("Cliente não encontrado.");
        }
        if (cartaoGateway.countByCliente(clienteOptional.get()) >= 2) {
            throw new ForbiddenErrorException("Número máximo de cartões atingido");
        }
        if (cartaoRequestDTO.limite() <= 0d) {
            throw new BusinessErrorException("Limite não pode ser negativo ou zero.");
        }
        try {
            mes = Integer.parseInt(cartaoRequestDTO.data_validade().substring(0,2));
            ano = Integer.parseInt(cartaoRequestDTO.data_validade().substring(3,5));
        } catch (Exception e) {
            throw new BusinessErrorException("Data de validade em formato inválido");
        }
        try {
            cvv = Integer.parseInt(cartaoRequestDTO.cvv());
        } catch (NumberFormatException ex) {
            throw new BusinessErrorException("CVV em formato inválido");
        }
        Cartao cartao = new Cartao(cartaoRequestDTO.numero().replaceAll(" ", ""),
                cartaoRequestDTO.limite(),cartaoRequestDTO.data_validade(),cvv,clienteOptional.get());

        cartaoGateway.create(cartao);

    }
}


/*{
      /*  "cpf":"1111111111",
        "limite":1000,
        "numero":"**** **** **** 1234",
        "data_validade":"12/24",
        "cvv":"123”
        }
        • Retorno:
        o 200 para sucesso
        o 401 para erro de autorização
        o 403 para erro número máximo de cartões atingido
        o 500 para um erro de negócio*/
