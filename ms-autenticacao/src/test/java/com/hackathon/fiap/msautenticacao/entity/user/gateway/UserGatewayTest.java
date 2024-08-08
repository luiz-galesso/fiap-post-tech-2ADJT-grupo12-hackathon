package com.hackathon.fiap.msautenticacao.entity.user.gateway;

import com.hackathon.fiap.msautenticacao.entity.user.model.User;
import com.hackathon.fiap.msautenticacao.infrastructure.user.repository.UserRepository;
import com.hackathon.fiap.msautenticacao.utils.UserHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserGatewayTest {
    AutoCloseable openMocks;
    private UserGateway userGateway;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        userGateway = new UserGateway(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirBuscarUserDetails() {
        User user = UserHelper.gerarUser("teste");
        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(user);

        var resultado = userGateway.findByUsername("teste");

        verify(userRepository, times(1))
                .findByUsername(any(String.class));
        assertThat(resultado)
                .isInstanceOf(UserDetails.class)
                .isNotNull();
    }


}
