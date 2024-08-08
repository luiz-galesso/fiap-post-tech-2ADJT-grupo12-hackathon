package com.hackathon.fiap.mscliente.entity.cliente.gateway;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.repository.ClienteRepository;
import com.hackathon.fiap.mscliente.infrastructure.cliente.utils.ClienteHelper;
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

class ClienteGatewayTest {
    AutoCloseable openMocks;
    private ClienteGateway clienteGateway;
    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        clienteGateway = new ClienteGateway(clienteRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCriarCliente() {
        Cliente cliente = ClienteHelper.gerarCliente(null);

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var resultado = clienteGateway.create(cliente);

        verify(clienteRepository, times(1)).save(cliente);
        assertThat(resultado)
                .isInstanceOf(Cliente.class)
                .isNotNull();
        assertThat(resultado)
                .usingRecursiveComparison()
                .isEqualTo(cliente);
    }

    @Test
    void devePermitirAlterarCliente() {
        Cliente clienteDesatualizado = ClienteHelper.gerarCliente("454.118.542-37");
        var clienteAtualizado = ClienteHelper.gerarCliente("454.118.542-37");
        clienteAtualizado.setNome("Isaias Camargo");

        when(clienteRepository.findById(any(String.class)))
                .thenReturn(Optional.of(clienteDesatualizado));
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(i -> i.getArgument(0));

        var resultado = clienteGateway.update(clienteAtualizado);

        assertThat(resultado)
                .isInstanceOf(Cliente.class)
                .isNotNull();

        assertNotEquals(resultado.getNome(), clienteDesatualizado.getNome());

        assertThat(resultado)
                .usingRecursiveComparison()
                .isEqualTo(clienteAtualizado);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void devePermitirRemoverCliente() {
        Cliente cliente = ClienteHelper.gerarCliente("454.118.542-37");
        doNothing().when(clienteRepository).deleteById(any(String.class));
        clienteGateway.remove(cliente.getCpf());
        var clienteOptional = clienteGateway.findById(cliente.getCpf());
        assertThat(clienteOptional).isEmpty();
        verify(clienteRepository, times(1)).deleteById(cliente.getCpf());
    }

    @Nested
    class ListarCliente {
        @Test
        void devePermitirBuscarCliente() {
            Cliente cliente = ClienteHelper.gerarCliente("454.118.542-37");
            when(clienteRepository.findById(any(String.class)))
                    .thenReturn(Optional.of(cliente));

            var resultado = clienteGateway.findById("454.118.542-37");

            verify(clienteRepository, times(1))
                    .findById(any(String.class));
            assertThat(resultado)
                    .isPresent()
                    .isInstanceOf(Optional.class)
                    .isNotNull();

            assertEquals(cliente, resultado.get());

            assertThat(resultado)
                    .usingRecursiveComparison()
                    .isEqualTo(Optional.of(cliente));
        }
    }
}
