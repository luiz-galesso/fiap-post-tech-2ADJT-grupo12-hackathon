package com.hackathon.fiap.mscliente.infrastructure.cartao.controller;

import com.hackathon.fiap.mscliente.entity.cartao.model.Cartao;
import com.hackathon.fiap.mscliente.infrastructure.cartao.controller.dto.CartaoRequestDTO;
import com.hackathon.fiap.mscliente.usecase.cartao.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartao")
@AllArgsConstructor
@Tag(name = "Cartão", description = "Serviços para manipular os cartões de um cliente")
public class CartaoController {

    private final CadastraCartao cadastraCartao;

    @Operation(summary = "Cadastro de cartão", description = "Serviço utilizado para cadastrar um novo cartão de um cliente.")
    @PostMapping(produces = "application/json")
    @Transactional
    public ResponseEntity<?> create(@RequestBody CartaoRequestDTO cartaoRequestDTO) {
        Cartao cartao = cadastraCartao.execute(cartaoRequestDTO);
        return new ResponseEntity<>(cartao, HttpStatus.CREATED);
    }

}
