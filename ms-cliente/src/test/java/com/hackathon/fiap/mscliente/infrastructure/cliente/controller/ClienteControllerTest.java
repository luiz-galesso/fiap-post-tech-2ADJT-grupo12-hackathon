package com.hackathon.fiap.mscliente.infrastructure.cliente.controller;

import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteResponseDTO;
import com.hackathon.fiap.mscliente.usecase.cliente.CadastraCliente;
import com.hackathon.fiap.mscliente.utils.ClienteHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.hackathon.fiap.mscliente.utils.ClienteHelper.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClienteControllerTest {

    @Mock
    private CadastraCliente cadastrarCliente;
    private MockMvc mockMvc;
    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        ClienteController clienteController = new ClienteController(cadastrarCliente);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }


    @Test
    @WithMockUser(username = "teste", roles = "integration")
    void devePermitirCadastrarCliente() throws Exception {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(clienteRequestDTO.cpf());
        when(cadastrarCliente.execute(any(ClienteRequestDTO.class))).thenReturn(clienteResponseDTO);

        mockMvc.perform(post("/api/cliente")
                .content(asJsonString(clienteRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(cadastrarCliente, times(1)).execute(any(ClienteRequestDTO.class));
    }

}
