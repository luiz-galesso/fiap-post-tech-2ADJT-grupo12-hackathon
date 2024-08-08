package com.hackathon.fiap.msautenticacao.infrastructure.user.controller;

import com.hackathon.fiap.msautenticacao.infrastructure.user.controller.dto.AuthenticationDTO;
import com.hackathon.fiap.msautenticacao.infrastructure.user.controller.dto.AuthenticationResponseDTO;
import com.hackathon.fiap.msautenticacao.usecase.user.AutenticaUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autenticacao")
@Tag(name = "Autenticação")
@AllArgsConstructor
public class AuthenticationController {

    private final AutenticaUsuario autenticaUsuario;

    @Operation(summary = "Autenticação", description = "Serviço utilizado para obter um token JWT para consumir os demais serviços.")
    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO dto){
        var token = autenticaUsuario.execute(dto.usuario(), dto.senha());
        return ResponseEntity.ok(new AuthenticationResponseDTO(token));
    }


}



