package com.hackathon.fiap.msautenticacao.infrastructure.user.repository;

import com.hackathon.fiap.msautenticacao.entity.user.model.User;
import com.hackathon.fiap.msautenticacao.utils.UserHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRepositoryTest {
    AutoCloseable openMocks;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirRegistrarUser() {
        User user = UserHelper.gerarUser("teste");
        when(userRepository.save(any(User.class))).thenAnswer(a -> a.getArguments()[0]);

        var userArmazenado = userRepository.save(user);
        verify(userRepository, times(1)).save(user);
        assertThat(userArmazenado)
                .isInstanceOf(User.class)
                .isNotNull();
        assertThat(userArmazenado)
                .usingRecursiveComparison()
                .isEqualTo(user);

    }

    @Test
    void devePermitirApagarUser() {
        String id = new Random().toString();
        doNothing().when(userRepository).deleteById(id);

        userRepository.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);

    }

    @Nested
    class ConsultarUsers {
        @Test
        void devePermitirConsultarUser() {

            String id = "teste";

            var user = UserHelper.gerarUser(id);

            when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

            var userOptional = userRepository.findById(id);

            verify(userRepository, times(1)).findById(id);
            assertThat(userOptional)
                    .isPresent()
                    .containsSame(user)
            ;
            userOptional.ifPresent(userArmazenado -> assertThat(userArmazenado)
                    .usingRecursiveComparison()
                    .isEqualTo(user));
        }
        @Test
        void devePermitirConsultarDetalhesUser() {

            String id = "teste";

            var user = UserHelper.gerarUser(id);

            when(userRepository.findByUsername(any(String.class))).thenReturn(user);

            var userDetails = userRepository.findByUsername(id);

            assertThat(userDetails)
                    .isInstanceOf(UserDetails.class) ;
            verify(userRepository, times(1)).findByUsername(any(String.class));

        }
        @Test
        void devePermitirListarUsers() {
            var user1 = UserHelper.gerarUser("teste");
            var user2 = UserHelper.gerarUser("teste 2");
            var userList = Arrays.asList(user1, user2);

            when(userRepository.findAll()).thenReturn(userList);

            var resultado = userRepository.findAll();

            verify(userRepository, times(1)).findAll();

            assertThat(resultado)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(user1, user2);
        }
    }
}
