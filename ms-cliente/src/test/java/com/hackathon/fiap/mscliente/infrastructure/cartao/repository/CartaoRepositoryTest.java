package com.hackathon.fiap.mscliente.infrastructure.cartao.repository;

import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cartao.utils.CartaoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartaoRepositoryTest {
    AutoCloseable openMocks;
    @Mock
    private CartaoRepository cartaoRepository;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirRegistrarCartao() {
        Cartao cartao = CartaoHelper.gerarCartao("5169 6900 4153 6370");
        when(cartaoRepository.save(any(Cartao.class))).thenAnswer(a -> a.getArguments()[0]);

        var cartaoArmazenado = cartaoRepository.save(cartao);
        verify(cartaoRepository, times(1)).save(cartao);
        assertThat(cartaoArmazenado)
                .isInstanceOf(Cartao.class)
                .isNotNull();
        assertThat(cartaoArmazenado)
                .usingRecursiveComparison()
                .isEqualTo(cartao);

    }

    @Test
    void devePermitirApagarCartao() {
        String id = new Random().toString();
        doNothing().when(cartaoRepository).deleteById(id);

        cartaoRepository.deleteById(id);

        verify(cartaoRepository, times(1)).deleteById(id);

    }

    @Nested
    class ConsultarCartoes {
        @Test
        void devePermitirConsultarCartao() {

            String id = "5169 6900 4153 6370";

            var cartao = CartaoHelper.gerarCartao(id);

            when(cartaoRepository.findById(any(String.class))).thenReturn(Optional.of(cartao));

            var cartaoOptional = cartaoRepository.findById(id);

            verify(cartaoRepository, times(1)).findById(id);
            assertThat(cartaoOptional)
                    .isPresent()
                    .containsSame(cartao)
            ;
            cartaoOptional.ifPresent(cartaoArmazenado -> assertThat(cartaoArmazenado)
                    .usingRecursiveComparison()
                    .isEqualTo(cartao));
        }

        @Test
        void devePermitirListarCartoes() {
            var cartao1 = CartaoHelper.gerarCartao("5169 6900 4153 6370");
            var cartao2 = CartaoHelper.gerarCartao("5157 6080 4584 0980");
            var cartaoList = Arrays.asList(cartao1, cartao2);

            when(cartaoRepository.findAll()).thenReturn(cartaoList);

            var resultado = cartaoRepository.findAll();

            verify(cartaoRepository, times(1)).findAll();

            assertThat(resultado)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(cartao1, cartao2);
        }
        @Test
        void devePermitirContarQuantosCartoesOClientePossui(){
            var cartao1 = CartaoHelper.gerarCartao("5169 6900 4153 6370");
            CartaoHelper.gerarCartao("5157 6080 4584 0980");
            when(cartaoRepository.countByCliente(any(Cliente.class)))
                    .thenReturn(2L);
            var cliente = cartao1.getCliente();

            var quantidadeCartoes = cartaoRepository.countByCliente(cliente);
            assertThat(quantidadeCartoes).isEqualTo(2L);

            verify(cartaoRepository, times(1)).countByCliente(any(Cliente.class));
        }
    }
}
