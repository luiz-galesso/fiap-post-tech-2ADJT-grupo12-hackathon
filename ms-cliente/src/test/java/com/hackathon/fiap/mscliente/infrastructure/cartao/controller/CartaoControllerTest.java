package com.hackathon.fiap.mscliente.infrastructure.cartao.controller;

import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteResponseDTO;
import com.hackathon.fiap.mscliente.usecase.cartao.ConsomeLimite;
import com.hackathon.fiap.mscliente.usecase.cartao.RegistraCartao;
import com.hackathon.fiap.mscliente.usecase.cartao.ValidaCartao;
import com.hackathon.fiap.mscliente.usecase.cliente.CadastraCliente;
import com.hackathon.fiap.mscliente.utils.CartaoHelper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartaoControllerTest {

    @Mock
    private RegistraCartao registraCartao;
    @Mock
    private ValidaCartao validaCartao;
    @Mock
    private ConsomeLimite consomeLimite;
    @Mock
    private CadastraCliente cadastrarCliente;
    private MockMvc mockMvc;
    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        CartaoController cartaoController = new CartaoController(registraCartao, validaCartao, consomeLimite);
        mockMvc = MockMvcBuilders.standaloneSetup(cartaoController)
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
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(clienteRequestDTO.cpf());
        when(cadastrarCliente.execute(any(ClienteRequestDTO.class))).thenReturn(clienteResponseDTO);

        mockMvc.perform(post("/api/cartao")
                .content(asJsonString(cartaoRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
        verify(registraCartao, times(1)).execute(any(CartaoRequestDTO.class));
    }

    @Test
    @WithMockUser(username = "teste", roles = "integration")
    void devePermitirConsumirLimite() throws Exception {
        ClienteRequestDTO clienteRequestDTO = ClienteHelper.gerarClienteRequest();
        //ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(clienteRequestDTO.cpf());
        CartaoRequestDTO cartaoRequestDTO = CartaoHelper.gerarClienteRequest(clienteRequestDTO.cpf());
        ConsomeLimiteRequestDTO consomeLimiteRequestDTO = new ConsomeLimiteRequestDTO(clienteRequestDTO.cpf(), cartaoRequestDTO.numero(), cartaoRequestDTO.data_validade(), cartaoRequestDTO.cvv(), 10D);

        mockMvc.perform(put("/api/cartao/limite")
                .content(asJsonString(consomeLimiteRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        verify(consomeLimite, times(1)).execute(any(ConsomeLimiteRequestDTO.class));
    }

}
