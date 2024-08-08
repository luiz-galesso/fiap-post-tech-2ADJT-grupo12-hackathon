package com.hackathon.fiap.msautenticacao.infrastructure.user.controller;

import com.hackathon.fiap.msautenticacao.infrastructure.user.controller.dto.AuthenticationDTO;
import com.hackathon.fiap.msautenticacao.usecase.user.AutenticaUsuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.hackathon.fiap.msautenticacao.utils.UserHelper.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {

    @Mock
    private AutenticaUsuario autenticaUsuario;
    private MockMvc mockMvc;
    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        AuthenticationController authenticationController = new AuthenticationController(autenticaUsuario);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void autenticaUsuario() throws Exception {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("teste", "teste1234");

        when(autenticaUsuario.execute(any(String.class), any(String.class))).thenReturn(null);

        mockMvc.perform(post("/api/autenticacao")
                .content(asJsonString(authenticationDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        verify(autenticaUsuario, times(1)).execute(any(String.class), any(String.class));
    }

}
