package com.hackathon.fiap.msautenticacao.usecase.user;

import com.hackathon.fiap.msautenticacao.entity.user.gateway.UserGateway;
import com.hackathon.fiap.msautenticacao.infrastructure.user.repository.UserRepository;
import com.hackathon.fiap.msautenticacao.utils.UserHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class AutenticaUsuarioIT {

    @Autowired
    AutenticaUsuario autenticaUsuario;
    @Autowired
    UserGateway userGateway;
    @Autowired
    UserRepository userRepository;

    @Test
    void autenticaUsuario() {
        var user = "adj2";
        var pwd = "adj@1234";

        UserDetails userDetails = userGateway.findByUsername(user);

        var token = autenticaUsuario.execute(userDetails.getUsername(), pwd);
        assertThat(token).isNotEmpty();
    }

    @Test
    void deveGerarExcecao_QuandoAutenticaUsuario_UsuarioOuSenhaInvalida() {
        var user = UserHelper.gerarUser("teste");
        userRepository.save(user);

        assertThatThrownBy(() -> autenticaUsuario.execute(user.getUsername(), user.getPassword()))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Usuário inexistente ou senha inválida");
    }

}
