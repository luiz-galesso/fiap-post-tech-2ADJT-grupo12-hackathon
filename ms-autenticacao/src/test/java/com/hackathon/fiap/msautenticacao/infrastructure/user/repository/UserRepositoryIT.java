package com.hackathon.fiap.msautenticacao.infrastructure.user.repository;

import com.hackathon.fiap.msautenticacao.entity.user.model.User;
import com.hackathon.fiap.msautenticacao.utils.UserHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UserRepositoryIT {
    @Autowired
    private UserRepository userRepository;

    @Test
    void devePermitirCriarTabela() {
        Long totalTabelasCriadas = userRepository.count();
        assertThat(totalTabelasCriadas).isNotNegative();
    }

    @Test
    void devePermitirRegistrarUser() {
        String id = "teste1";
        var user = UserHelper.gerarUser(id);
        var userArmazenado = userRepository.save(user);

        assertThat(userArmazenado)
                .isInstanceOf(User.class)
                .isNotNull();

        assertThat(userArmazenado)
                .usingRecursiveComparison()
                .isEqualTo(user);

        assertThat(userArmazenado)
                .isNotNull();
    }

    @Test
    void devePermitirApagarUser() {
        var userGerado = UserHelper.gerarUser("teste1");
        var user = UserHelper.registrarUser(userRepository, userGerado);
        var id = user.getUsername();

        userRepository.deleteById(id);

        var userOptional = userRepository.findById(id);

        assertThat(userOptional)
                .isEmpty();
    }


    @Nested
    class ConsultarUsers {
        @Test
        void devePermitirListarUsers() {

            UserHelper.registrarUser(userRepository, UserHelper.gerarUser("teste1"));
            UserHelper.registrarUser(userRepository, UserHelper.gerarUser("teste2"));
            UserHelper.registrarUser(userRepository, UserHelper.gerarUser("teste3"));
            var resultado = userRepository.findAll();

            assertThat(resultado)
                    .hasSize(4);
        }

        @Test
        void devePermitirConsultarDetalhesUser(){
            var userGerado = UserHelper.gerarUser("teste1");
            var user = UserHelper.registrarUser(userRepository, userGerado);

            var username = user.getUsername();
            var userDetails = userRepository.findByUsername(username);

            assertThat(userDetails)
                    .isInstanceOf(UserDetails.class);


        }
        @Test
        void devePermitirConsultarUserPeloId() {
            var userGerado = UserHelper.gerarUser("teste1");
            var user = UserHelper.registrarUser(userRepository, userGerado);

            var username = user.getUsername();

            var userOptional = userRepository.findById(username);
            assertThat(userOptional)
                    .isPresent()
                    .containsSame(user);

            userOptional.ifPresent(userArmazenado -> assertThat(userArmazenado)
                    .usingRecursiveComparison()
                    .isEqualTo(user));
        }

    }
}
