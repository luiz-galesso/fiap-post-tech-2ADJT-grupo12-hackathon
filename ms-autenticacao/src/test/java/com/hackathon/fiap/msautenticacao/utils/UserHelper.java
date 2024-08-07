package com.hackathon.fiap.msautenticacao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.fiap.msautenticacao.entity.user.model.User;
import com.hackathon.fiap.msautenticacao.entity.user.model.enums.UserRole;
import com.hackathon.fiap.msautenticacao.infrastructure.user.repository.UserRepository;

public class UserHelper {

    public static User gerarUser(String username) {
        return User.builder()
                .username(username)
                .password("teste")
                .role(UserRole.ADMIN).build();
    }

    public static User registrarUser(UserRepository userRepository, User user) {
        return userRepository.save(user);
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }
}
