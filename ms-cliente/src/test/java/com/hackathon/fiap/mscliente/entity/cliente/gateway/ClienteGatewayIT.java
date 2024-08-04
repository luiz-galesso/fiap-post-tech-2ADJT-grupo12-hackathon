package com.hackathon.fiap.mscliente.entity.cliente.gateway;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.repository.ClienteRepository;
import com.hackathon.fiap.mscliente.infrastructure.cliente.utils.ClienteHelper;
import jakarta.transaction.Transactional;
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
class ClienteGatewayIT {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteGateway clienteGateway;

    @Test
    void devePermitirCriarCliente() {
        var cliente = ClienteHelper.gerarCliente("454.118.542-37");

        var clienteArmazenado = clienteGateway.create(cliente);

        assertThat(clienteArmazenado)
                .isNotNull()
                .isInstanceOf(Cliente.class);

        assertThat(clienteArmazenado)
                .usingRecursiveComparison()
                .isEqualTo(cliente);

        assertThat(clienteArmazenado.getCpf())
                .isNotNull();
    }

    @Test
    void devePermitirAlterarCliente() {

        var clienteDesatualizado = ClienteHelper.registrarCliente(clienteRepository, ClienteHelper.gerarCliente("454.118.542-37"));
        var nomeAntigo = clienteDesatualizado.getNome();
        var clienteAtualizado = clienteDesatualizado.toBuilder().nome("Jardins Grill Prime").build();

        var resultado = clienteGateway.update(clienteAtualizado);

        assertThat(resultado)
                .isInstanceOf(Cliente.class)
                .isNotNull();

        assertNotEquals(resultado.getNome(), nomeAntigo);
        assertEquals(resultado.getCpf(), clienteDesatualizado.getCpf());

        assertThat(resultado)
                .usingRecursiveComparison()
                .isEqualTo(clienteAtualizado);
    }

    @Test
    void devePermitirDeletar() {
        var cliente = ClienteHelper.registrarCliente(clienteRepository, ClienteHelper.gerarCliente("454.118.542-37"));

        clienteGateway.remove(cliente.getCpf());
        var clienteOptional = clienteGateway.findById(cliente.getCpf());
        assertThat(clienteOptional)
                .isEmpty();
    }

    @Nested
    class ListarCliente {
        @Test
        void devePermitirListarClientePorCpf() {
            var cliente = ClienteHelper.registrarCliente(clienteRepository, ClienteHelper.gerarCliente("454.118.542-37"));

            var clienteOptional = clienteGateway.findById(cliente.getCpf());

            assertThat(clienteOptional)
                    .isPresent()
                    .containsSame(cliente);
            clienteOptional.ifPresent(clienteObtido -> assertThat(clienteObtido)
                    .usingRecursiveComparison()
                    .isEqualTo(cliente));
        }
    }
}
