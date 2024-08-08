package com.hackathon.fiap.mscliente.infrastructure.cartao.repository;

import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
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

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class CartaoRepositoryIT {
    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void criarCliente() {
        ClienteHelper.registrarCliente(clienteRepository, ClienteHelper.gerarCliente("454.118.542-37"));
    }

    @Test
    void devePermitirCriarTabela() {
        Long totalTabelasCriadas = cartaoRepository.count();
        assertThat(totalTabelasCriadas).isNotNegative();
    }

    @Test
    void devePermitirRegistrarCartao() {
        String id = "5169 6900 4153 6370";
        var cartao = CartaoHelper.gerarCartao(id);
        var cartaoArmazenado = cartaoRepository.save(cartao);

        assertThat(cartaoArmazenado)
                .isInstanceOf(Cartao.class)
                .isNotNull();

        assertThat(cartaoArmazenado)
                .usingRecursiveComparison()
                .isEqualTo(cartao);

        assertThat(cartaoArmazenado.getNumeroCartao())
                .isNotNull();
    }

    @Test
    void devePermitirApagarCartao() {
        var cartaoGerado = CartaoHelper.gerarCartao("5169 6900 4153 6370");
        var cartao = CartaoHelper.registrarCartao(cartaoRepository, cartaoGerado);
        var id = cartao.getNumeroCartao();

        cartaoRepository.deleteById(id);

        var cartaoOptional = cartaoRepository.findById(id);

        assertThat(cartaoOptional)
                .isEmpty();
    }


    @Nested
    class ConsultarCartaos {
        @Test
        void devePermitirListarCartoes() {

            CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5169 6900 4153 6370"));
            CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5157 6080 4584 0980"));
            CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5353 0885 1602 6744"));
            var resultado = cartaoRepository.findAll();

            assertThat(resultado)
                    .hasSize(3);
        }

        @Test
        void devePermitirConsultarCartaoPeloNumero() {
            var cartaoGerado = CartaoHelper.gerarCartao("5169 6900 4153 6370");
            var cartao = CartaoHelper.registrarCartao(cartaoRepository, cartaoGerado);

            var numeroCartao = cartao.getNumeroCartao();

            var cartaoOptional = cartaoRepository.findById(numeroCartao);
            assertThat(cartaoOptional)
                    .isPresent()
                    .containsSame(cartao);

            cartaoOptional.ifPresent(cartaoArmazenado -> assertThat(cartaoArmazenado)
                    .usingRecursiveComparison()
                    .isEqualTo(cartao));
        }

        @Test
        void devePermitirContarQuantosCartoesOClientePossui() {
            var cartao1 = CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5169 6900 4153 6370"));
            CartaoHelper.registrarCartao(cartaoRepository, CartaoHelper.gerarCartao("5157 6080 4584 0980"));

            var cliente = cartao1.getCliente();

            var quantidadeCartoes = cartaoRepository.countByCliente(cliente);
            assertThat(quantidadeCartoes).isEqualTo(2L);

        }

    }
}
