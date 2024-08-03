package com.hackathon.fiap.mscliente.infrastructure.cliente.controller;

import com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto.ClienteRequestDTO;
import com.hackathon.fiap.mscliente.usecase.cliente.CadastraCliente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
@Tag(name = "Cliente", description = "Serviços para manipular os clientes")
@AllArgsConstructor
public class ClienteController {

    private final CadastraCliente cadastraCliente;

    @Operation(summary = "Cadastro de cliente", description = "Serviço utilizado para criar um novo cliente.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(produces = "application/json")
    @PreAuthorize("hasRole('INTEGRATION')")
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        var clienteResponseDTO = cadastraCliente.execute(clienteRequestDTO);
        return new ResponseEntity<>(clienteResponseDTO, HttpStatus.OK);
    }

}
