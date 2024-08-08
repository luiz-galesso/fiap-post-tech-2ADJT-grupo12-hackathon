package com.hackathon.fiap.msautenticacao.entity.user.gateway;

import com.hackathon.fiap.msautenticacao.infrastructure.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
@AllArgsConstructor
public class UserGateway {

    private UserRepository userRepository;

    public UserDetails findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }



}