package com.hackathon.fiap.mscliente.infrastructure.cliente.repository;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import com.hackathon.fiap.mscliente.infrastructure.cliente.utils.ClienteHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ClienteRepositoryIT {
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void devePermitirCriarTabela() {
        Long totalTabelasCriadas = clienteRepository.count();
        assertThat(totalTabelasCriadas).isNotNegative();
    }

    @Test
    void devePermitirRegistrarCliente() {
        String id = "454.118.542-37";
        var cliente = ClienteHelper.gerarCliente(id);
        var clienteArmazenado = clienteRepository.save(cliente);

        assertThat(clienteArmazenado)
                .isInstanceOf(Cliente.class)
                .isNotNull();

        assertThat(clienteArmazenado)
                .usingRecursiveComparison()
                .isEqualTo(cliente);

        assertThat(clienteArmazenado.getCpf())
                .isNotNull();
    }

    @Test
    void devePermitirApagarCliente() {
        var clienteGerado = ClienteHelper.gerarCliente("454.118.542-37");
        var cliente = ClienteHelper.registrarCliente(clienteRepository, clienteGerado);
        var id = cliente.getCpf();

        clienteRepository.deleteById(id);

        var clienteOptional = clienteRepository.findById(id);

        assertThat(clienteOptional)
                .isEmpty();
    }


    @Nested
    class ConsultarClientes {
        @Test
        void devePermitirListarClientes() {

            ClienteHelper.registrarCliente(clienteRepository, ClienteHelper.gerarCliente("454.118.542-37"));
            ClienteHelper.registrarCliente(clienteRepository, ClienteHelper.gerarCliente("726.385.369-89"));
            ClienteHelper.registrarCliente(clienteRepository, ClienteHelper.gerarCliente("476.284.843-36"));
            var resultado = clienteRepository.findAll();

            assertThat(resultado)
                    .hasSize(3);
        }

        @Test
        void devePermitirConsultarClientePeloCpf() {
            var clienteGerado = ClienteHelper.gerarCliente("454.118.542-37");
            var cliente = ClienteHelper.registrarCliente(clienteRepository, clienteGerado);

            var cpf = cliente.getCpf();

            var clienteOptional = clienteRepository.findById(cpf);
            assertThat(clienteOptional)
                    .isPresent()
                    .containsSame(cliente);

            clienteOptional.ifPresent(clienteArmazenado -> assertThat(clienteArmazenado)
                    .usingRecursiveComparison()
                    .isEqualTo(cliente));
        }


    }
}
