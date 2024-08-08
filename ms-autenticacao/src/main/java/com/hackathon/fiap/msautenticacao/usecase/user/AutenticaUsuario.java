package com.hackathon.fiap.msautenticacao.usecase.user;

import com.hackathon.fiap.msautenticacao.entity.user.model.User;
import com.hackathon.fiap.msautenticacao.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AutenticaUsuario {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    public String execute(String usuario, String senha) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(usuario, senha);
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return tokenService.generateToken((User) auth.getPrincipal());
    }
}
