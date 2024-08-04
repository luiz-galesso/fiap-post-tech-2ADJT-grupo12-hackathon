package com.hackathon.fiap.mscliente.entity.cartao.gateway;

import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.infrastructure.cartao.repository.CartaoRepository;
import com.hackathon.fiap.mscliente.infrastructure.cartao.utils.CartaoHelper;
import com.hackathon.fiap.mscliente.infrastructure.cliente.repository.ClienteRepository;
import com.hackathon.fiap.mscliente.infrastructure.cliente.utils.ClienteHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class CartaoGatewayIT {
    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CartaoGateway cartaoGateway;

    @BeforeEach
    void criarCliente() {
        ClienteHelper.registrarCliente(clienteRepository, ClienteHelper.gerarCliente("454.118.542-37"));
    }

    @Test
    void devePermitirCriarCartao() {
        var cartao = CartaoHelper.gerarCartao("5169 6900 4153 6370");

        var cartaoArmazenado = cartaoGateway.create(cartao);

        assertThat(cartaoArmazenado)
                .isNotNull()
                .isInstanceOf(Cartao.class);

        assertThat(cartaoArmazenado)
                .usingRecursiveComparison()
                .isEqualTo(cartao);

        assertThat(cartaoArmazenado.getNumeroCartao())
                .isNotNull();
    }
    @Test
    void devePermitirAlterarCartao() {

        var cartaoDesatualizado = CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5169 6900 4153 6370"));
        var limiteAntigo = cartaoDesatualizado.getLimite();
        var cartaoAtualizado = cartaoDesatualizado.toBuilder().limite(1000.00).build();

        var resultado = cartaoGateway.update(cartaoAtualizado);

        assertThat(resultado)
                .isInstanceOf(Cartao.class)
                .isNotNull();

        assertNotEquals(resultado.getLimite(), limiteAntigo);
        assertEquals(resultado.getNumeroCartao(), cartaoDesatualizado.getNumeroCartao());

        assertThat(resultado)
                .usingRecursiveComparison()
                .isEqualTo(cartaoAtualizado);
    }

    @Nested
    class ListarCartao {
        @Test
        void devePermitirListarCartaoPorCpf() {
            var cartao = CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5169 6900 4153 6370"));

            var cartaoOptional = cartaoGateway.findById(cartao.getNumeroCartao());

            assertThat(cartaoOptional)
                    .isPresent()
                    .containsSame(cartao);
            cartaoOptional.ifPresent(cartaoObtido -> assertThat(cartaoObtido)
                    .usingRecursiveComparison()
                    .isEqualTo(cartao));
        }

        @Test
        void devePermitirContarQuantosCartoesOClientePossui(){
            var cartao1 = CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5169 6900 4153 6370"));
            CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5157 6080 4584 0980"));

            var cliente = cartao1.getCliente();

            var quantidadeCartoes = cartaoGateway.countByCliente(cliente);
            assertThat(quantidadeCartoes).isEqualTo(2L);

        }
    }
}
