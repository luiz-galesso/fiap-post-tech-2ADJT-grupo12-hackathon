package com.hackathon.fiap.msautenticacao.entity.user.gateway;

import com.hackathon.fiap.msautenticacao.infrastructure.user.repository.UserRepository;
import com.hackathon.fiap.msautenticacao.utils.UserHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UserGatewayIT {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGateway userGateway;

    @Test
    void devePermitirListarUserPorCpf() {
        var user = UserHelper.registrarUser(userRepository, UserHelper.gerarUser("teste"));

        var userDetails = userGateway.findByUsername(user.getUsername());

        assertThat(userDetails)
                .isInstanceOf(UserDetails.class);
    }
}
