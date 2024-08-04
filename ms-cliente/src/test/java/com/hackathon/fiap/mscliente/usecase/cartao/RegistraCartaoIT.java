package com.hackathon.fiap.mscliente.usecase.cartao;

import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
import com.hackathon.fiap.mscliente.usecase.exception.ForbiddenErrorException;
import com.hackathon.fiap.mscliente.utils.CartaoHelper;
import com.hackathon.fiap.mscliente.utils.ClienteHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class RegistraCartaoIT {

    @Autowired
    RegistraCartao registraCartao;
    @Autowired
    CartaoGateway cartaoGateway;
    @Autowired
    ClienteGateway clienteGateway;

    @Test
    void devePermitirRegistrarCartao() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);

        registraCartao.execute(cartaoRequestDTO);
        var retorno = cartaoGateway.findById(cartaoRequestDTO.numero());
        assertThat(retorno).isInstanceOf(Optional.class);
        assertThat(retorno).isNotNull();
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_CartaoJaCadastrado() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);

        registraCartao.execute(cartaoRequestDTO);

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("Já existe cartão com o número informado.");
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_NumeroMaximoCartaoCadastrado() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);
        registraCartao.execute(cartaoRequestDTO);
        CartaoRequestDTO cartaoRequestDTO2 = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        registraCartao.execute(cartaoRequestDTO2);
        CartaoRequestDTO cartaoRequestDTO3 = CartaoHelper.gerarClienteRequest(cliente.getCpf());

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO3))
                .isInstanceOf(ForbiddenErrorException.class)
                .hasMessage("Número máximo de cartões atingido");
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_LimiteNegativoOuZero() {
        Cliente cliente = ClienteHelper.gerarCliente();
        clienteGateway.create(cliente);
        CartaoRequestDTO cartaoRequestDTO = new CartaoRequestDTO(cliente.getCpf(), 0D, "1234 5678 9012 3456", "12/24", "123");

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("Limite não pode ser negativo ou zero.");
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_DataValidadeInvalida() {
        Cliente cliente = ClienteHelper.gerarCliente();
        clienteGateway.create(cliente);
        CartaoRequestDTO cartaoRequestDTO = new CartaoRequestDTO(cliente.getCpf(), 1000.00, "6421 5031 9012 5471", "12/A2024", "123");

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("Data de validade em formato inválido");
    }

    @Test
    void deveGerarExcecao_QuandoRegistrarCartao_ClienteNaoCadastrado() {
        Cliente cliente = ClienteHelper.gerarCliente();
        clienteGateway.create(cliente);
        CartaoRequestDTO cartaoRequestDTO = new CartaoRequestDTO(cliente.getCpf(), 1500.00, "8471 5678 9012 5148", "12/24", "A123");

        assertThatThrownBy(() -> registraCartao.execute(cartaoRequestDTO))
                .isInstanceOf(BusinessErrorException.class)
                .hasMessage("CVV em formato inválido");
    }

}
