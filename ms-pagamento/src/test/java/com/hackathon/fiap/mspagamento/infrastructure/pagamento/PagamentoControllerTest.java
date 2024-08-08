package com.hackathon.fiap.mspagamento.infrastructure.pagamento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.fiap.mspagamento.infrastructure.exception.ControllerExceptionHandler;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.PagamentoController;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.usecase.pagamento.ObtemPagamentosPorCliente;
import com.hackathon.fiap.mspagamento.usecase.pagamento.RealizaPagamento;
import com.hackathon.fiap.mspagamento.utils.PagamentoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PagamentoControllerTest {

    @Mock
    private RealizaPagamento realizaPagamento;

    @Mock
    private ObtemPagamentosPorCliente obtemPagamentosPorCliente;

    private MockMvc mockMvc;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        PagamentoController reservaController = new PagamentoController(realizaPagamento, obtemPagamentosPorCliente);
        mockMvc = MockMvcBuilders.standaloneSetup(reservaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void devePermitirRealizarPagamento() throws Exception {

        //arrange
        String token = "258317937s54dzsadas21122@#$@@!asdkhak";
        var pagamentoRequestDTO = PagamentoHelper.gerarPagamentoRequestDTO();
        var pagamento = PagamentoHelper.gerarPagamento();

        when(realizaPagamento.execute(anyString(), any(PagamentoRequestDTO.class)))
                .thenReturn(pagamento);

        //act
        mockMvc.perform(
                post("/api/pagamentos").header("Authorization", "Bearer " + token)
                    .content(asJsonString(pagamentoRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk());

        //assert
        verify(realizaPagamento, times(1)).execute(anyString(), any(PagamentoRequestDTO.class));
    }

    @Test
    void devePermitirObterTodosOsPagamentosDeUmCliente() throws Exception {
        //arrange
        String token = "258317937s54dzsadas21122@#$@@!asdkhak";
        var pagamentos = PagamentoHelper.criarLista(3);
        var pagamentosLista = PagamentoHelper.gerarLista(pagamentos);

        when(obtemPagamentosPorCliente.execute(anyString())).thenReturn(pagamentosLista);

        //act
        mockMvc.perform(
                get("/api/pagamentos/cliente/{chave}", "12345678909")
                        .header("Authorization", "Bearer " + token)
        ).andExpect(status().isOk());

        //assert
        verify(obtemPagamentosPorCliente, times(1)).execute(anyString());
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }
}
