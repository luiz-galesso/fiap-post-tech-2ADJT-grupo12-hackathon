package com.hackathon.fiap.mscliente.infrastructure.cliente.repository;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.utils.ClienteHelper;
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

class ClienteRepositoryTest {
    AutoCloseable openMocks;
    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirRegistrarCliente() {
        Cliente cliente = ClienteHelper.gerarCliente("454.118.542-37");
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(a -> a.getArguments()[0]);

        var clienteArmazenado = clienteRepository.save(cliente);
        verify(clienteRepository, times(1)).save(cliente);
        assertThat(clienteArmazenado)
                .isInstanceOf(Cliente.class)
                .isNotNull();
        assertThat(clienteArmazenado)
                .usingRecursiveComparison()
                .isEqualTo(cliente);

    }

    @Test
    void devePermitirApagarCliente() {
        String id = new Random().toString();
        doNothing().when(clienteRepository).deleteById(id);

        clienteRepository.deleteById(id);

        verify(clienteRepository, times(1)).deleteById(id);

    }

    @Nested
    class ConsultarClientees {
        @Test
        void devePermitirListarClientes() {
            var cliente1 = ClienteHelper.gerarCliente(null);
            var cliente2 = ClienteHelper.gerarCliente(null);
            var clienteList = Arrays.asList(cliente1, cliente2);

            when(clienteRepository.findAll()).thenReturn(clienteList);

            var resultado = clienteRepository.findAll();

            verify(clienteRepository, times(1)).findAll();

            assertThat(resultado)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(cliente1, cliente2);
        }

        @Test
        void devePermitirConsultarClientePorCpf() {
            var cliente = ClienteHelper.gerarCliente(null);
            var cpf = "454.118.542-37";

            when(clienteRepository.findById(any(String.class))).thenReturn(Optional.of(cliente));

            var clienteOptional = clienteRepository.findById(cpf);

            verify(clienteRepository, times(1)).findById(cpf);
            assertThat(clienteOptional)
                    .isPresent()
                    .containsSame(cliente)
            ;
            clienteOptional.ifPresent(clienteArmazenado -> assertThat(clienteArmazenado)
                    .usingRecursiveComparison()
                    .isEqualTo(cliente));

        }
    }
}
