package com.hackathon.fiap.msautenticacao.usecase.user;

import com.hackathon.fiap.msautenticacao.entity.user.model.User;
import com.hackathon.fiap.msautenticacao.infrastructure.user.repository.UserRepository;
import com.hackathon.fiap.msautenticacao.security.TokenService;
import com.hackathon.fiap.msautenticacao.utils.UserHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AutenticaUsuarioTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AutenticaUsuario autenticaUsuario;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        autenticaUsuario = new AutenticaUsuario(authenticationManager, tokenService);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void autenticaUsuario() {
        var user = UserHelper.gerarUser("teste");
        when(userRepository.save(any(User.class))).thenReturn((user));

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        when(tokenService.generateToken(any(User.class))).thenReturn(user.getUsername() + user.getPassword());

        var token = autenticaUsuario.execute(user.getUsername(), user.getPassword());

        assertThat(token).isNotEmpty();
        verify(tokenService, times(1)).generateToken(any(User.class));
    }

    @Test
    void deveGerarExcecao_QuandoAutenticaUsuario_UsuarioOuSenhaInvalida() {
        var user = UserHelper.gerarUser("teste");

        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Usuário inexistente ou senha inválida"));

        assertThatThrownBy(() -> autenticaUsuario.execute(user.getUsername(), user.getPassword()))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Usuário inexistente ou senha inválida");

        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
    }
}
