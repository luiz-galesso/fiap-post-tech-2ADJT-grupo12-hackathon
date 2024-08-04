package com.hackathon.fiap.mscliente.entity.cartao.gateway;

import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.repository.CartaoRepository;
import com.hackathon.fiap.mscliente.infrastructure.cartao.utils.CartaoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartaoGatewayTest {
    AutoCloseable openMocks;
    private CartaoGateway cartaoGateway;
    @Mock
    private CartaoRepository cartaoRepository;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        cartaoGateway = new CartaoGateway(cartaoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCriarCartao() {
        Cartao cartao = CartaoHelper.gerarCartao("5169 6900 4153 6370");

        when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartao);

        var resultado = cartaoGateway.create(cartao);

        verify(cartaoRepository, times(1)).save(cartao);
        assertThat(resultado)
                .isInstanceOf(Cartao.class)
                .isNotNull();
        assertThat(resultado)
                .usingRecursiveComparison()
                .isEqualTo(cartao);
    }

    @Test
    void devePermitirAlterarCartao() {
        Cartao cartaoDesatualizado = CartaoHelper.gerarCartao("5169 6900 4153 6370");
        var cartaoAtualizado = CartaoHelper.gerarCartao("5169 6900 4153 6370");
        cartaoAtualizado.setLimite(1000.00);

        when(cartaoRepository.findById(any(String.class)))
                .thenReturn(Optional.of(cartaoDesatualizado));
        when(cartaoRepository.save(any(Cartao.class)))
                .thenAnswer(i -> i.getArgument(0));

        var resultado = cartaoGateway.update(cartaoAtualizado);

        assertThat(resultado)
                .isInstanceOf(Cartao.class)
                .isNotNull();

        assertNotEquals(resultado.getLimite(), cartaoDesatualizado.getLimite());

        assertThat(resultado)
                .usingRecursiveComparison()
                .isEqualTo(cartaoAtualizado);
        verify(cartaoRepository, times(1)).save(any(Cartao.class));
    }

    @Nested
    class ListarCartao {
        @Test
        void devePermitirBuscarCartao() {
            Cartao cartao = CartaoHelper.gerarCartao("5169 6900 4153 6370");
            when(cartaoRepository.findById(any(String.class)))
                    .thenReturn(Optional.of(cartao));

            var resultado = cartaoGateway.findById("5169 6900 4153 6370");

            verify(cartaoRepository, times(1))
                    .findById(any(String.class));
            assertThat(resultado)
                    .isPresent()
                    .isInstanceOf(Optional.class)
                    .isNotNull();

            assertEquals(cartao, resultado.get());

            assertThat(resultado)
                    .usingRecursiveComparison()
                    .isEqualTo(Optional.of(cartao));
        }

        @Test
        void devePermitirContarQuantosCartoesOClientePossui() {
            var cartao1 = CartaoHelper.gerarCartao("5169 6900 4153 6370");
            CartaoHelper.gerarCartao("5157 6080 4584 0980");
            when(cartaoRepository.countByCliente(any(Cliente.class)))
                    .thenReturn(2L);
            var cliente = cartao1.getCliente();

            var quantidadeCartoes = cartaoGateway.countByCliente(cliente);
            assertThat(quantidadeCartoes).isEqualTo(2L);
            verify(cartaoRepository, times(1)).countByCliente(any(Cliente.class));
        }
    }
}
