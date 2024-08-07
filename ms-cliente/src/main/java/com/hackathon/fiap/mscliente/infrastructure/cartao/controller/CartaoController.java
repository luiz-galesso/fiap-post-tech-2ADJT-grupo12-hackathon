package com.hackathon.fiap.mscliente.infrastructure.cartao.controller;

import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteRequestDTO;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.ConsomeLimiteResponseDTO;
import com.hackathon.fiap.mscliente.infrastructure.util.DefaultResponse;
import com.hackathon.fiap.mscliente.usecase.cartao.ConsomeLimite;
import com.hackathon.fiap.mscliente.usecase.cartao.RegistraCartao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/cartao")
@AllArgsConstructor
@Tag(name = "Cartão", description = "Serviços para manipular os cartões de um cliente")
public class CartaoController {

    private final RegistraCartao registraCartao;
    private final ConsomeLimite consomeLimite;

    @Operation(summary = "Registro de cartão", description = "Serviço utilizado para cadastrar um novo cartão de um cliente.")
    @PostMapping(produces = "application/json")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('INTEGRATION')")
    @Transactional
    public ResponseEntity<?> create(@RequestBody CartaoRequestDTO cartaoRequestDTO) {
        registraCartao.execute(cartaoRequestDTO);
        return new ResponseEntity<>(new DefaultResponse(Instant.now(),"OK", null), HttpStatus.CREATED);
    }

    @Operation(summary = "Consome limite do Cartão", description = "Serviço utilizado para consumir o limite de um cartão.")
    @PutMapping(path = "/limite", produces = "application/json")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('INTEGRATION')")
    @Transactional
    public ResponseEntity<?> consomeLimite(@RequestBody ConsomeLimiteRequestDTO consomeLimiteRequestDTO) {
        ConsomeLimiteResponseDTO response = consomeLimite.execute(consomeLimiteRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
