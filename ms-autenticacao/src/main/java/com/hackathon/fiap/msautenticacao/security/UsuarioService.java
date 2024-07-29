package com.hackathon.fiap.msautenticacao.security;

import com.hackathon.fiap.msautenticacao.entity.user.gateway.UserGateway;
import com.hackathon.fiap.msautenticacao.infrastructure.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    UserGateway userGateway;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userGateway.findByUsername(email);
    }
}