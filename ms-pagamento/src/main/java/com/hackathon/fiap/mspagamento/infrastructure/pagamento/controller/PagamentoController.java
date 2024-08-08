package com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.usecase.pagamento.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamento")
@AllArgsConstructor
@Tag(name = "Pagamento", description = "Serviços para manipular os pagamentos de um cliente")
public class PagamentoController {

    private final RealizaPagamento realizaPagamento;

    @Operation(summary = "Pagamento", description = "Serviço utilizado para realizar um novo pagamento.")
    @PostMapping(produces = "application/json")
    @Transactional
    public ResponseEntity<?> create(@RequestBody PagamentoRequestDTO pagamentoRequestDTO) {
        Pagamento pagamento = realizaPagamento.execute(pagamentoRequestDTO);
        return new ResponseEntity<>(pagamento, HttpStatus.CREATED);
    }

}
