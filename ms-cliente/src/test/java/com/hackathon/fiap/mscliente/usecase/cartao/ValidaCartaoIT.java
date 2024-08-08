package com.hackathon.fiap.mscliente.usecase.cartao;

import com.hackathon.fiap.mscliente.entity.cartao.gateway.CartaoGateway;
import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.gateway.ClienteGateway;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ValidaCartaoResponseDTO;
import com.hackathon.fiap.mscliente.utils.CartaoHelper;
import com.hackathon.fiap.mscliente.utils.ClienteHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ValidaCartaoIT {

    @Autowired
    ValidaCartao validaCartao;
    @Autowired
    CartaoGateway cartaoGateway;
    @Autowired
    ClienteGateway clienteGateway;

    @Test
    void devePermitirValidarCartao() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));

        ValidaCartaoResponseDTO responseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), cartaoRequestDTO.data_validade(), cartaoRequestDTO.cvv());
        assertThat(responseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.message()).isEqualToIgnoringCase("Cartão validado");
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CartaoNaoExiste() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);

        ValidaCartaoResponseDTO responseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), cartaoRequestDTO.data_validade(), cartaoRequestDTO.cvv());
        assertThat(responseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.message()).isEqualToIgnoringCase("Não existe cartão com o número informado");
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CartaoNaoPertenceCliente() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));

        ValidaCartaoResponseDTO responseDTO = validaCartao.execute(cartaoRequestDTO.numero(), "11122233344", cartaoRequestDTO.data_validade(), cartaoRequestDTO.cvv());
        assertThat(responseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.message()).isEqualToIgnoringCase("Cartão não pertence ao cliente.");
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_DataValidadeDiferente() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));

        ValidaCartaoResponseDTO responseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), "01-29", cartaoRequestDTO.cvv());
        assertThat(responseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.message()).isEqualToIgnoringCase("Data de validade informada não é igual a data de validade do cartão");

    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CartaoVencido() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), "01-22", Integer.parseInt(cartaoRequestDTO.cvv()), cliente));

        ValidaCartaoResponseDTO responseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), "01-22", cartaoRequestDTO.cvv());
        assertThat(responseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.message()).isEqualToIgnoringCase("Cartão vencido");
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CvvInvalido() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));

        ValidaCartaoResponseDTO responseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), cartaoRequestDTO.data_validade(), "001");
        assertThat(responseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.message()).isEqualToIgnoringCase("CVV inválido");
    }

    @Test
    void deveRetornarMensagemErro_QuandoValidarCartao_CvvFormatoInvalido() {
        Cliente cliente = ClienteHelper.gerarCliente();
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(cliente.getCpf());
        clienteGateway.create(cliente);
        cartaoGateway.create(new Cartao(cartaoRequestDTO.numero(), cartaoRequestDTO.limite(), cartaoRequestDTO.data_validade(), Integer.parseInt(cartaoRequestDTO.cvv()), cliente));

        ValidaCartaoResponseDTO responseDTO = validaCartao.execute(cartaoRequestDTO.numero(), cliente.getCpf(), cartaoRequestDTO.data_validade(), "A201");
        assertThat(responseDTO).isInstanceOf(ValidaCartaoResponseDTO.class);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.message()).isEqualToIgnoringCase("CVV em formato inválido");
    }

}
