package com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller;

import com.hackathon.fiap.mspagamento.entity.pagamento.model.Pagamento;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoChaveDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoRequestDTO;
import com.hackathon.fiap.mspagamento.infrastructure.pagamento.controller.dto.PagamentoResponseDTO;
import com.hackathon.fiap.mspagamento.usecase.pagamento.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@AllArgsConstructor
@Tag(name = "Pagamento", description = "Serviços para manipular os pagamentos de um cliente")
public class PagamentoController {

    private final RealizaPagamento realizaPagamento;
    private final ObtemPagamentosPorCliente obtemPagamentosPorCliente;

    @Operation(summary = "Pagamento", description = "Serviço utilizado para realizar um novo pagamento.")
    @PostMapping(produces = "application/json")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('INTEGRATION')")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody PagamentoRequestDTO pagamentoRequestDTO) {
        Pagamento pagamento = realizaPagamento.execute(request.getHeader("Authorization"), pagamentoRequestDTO);
        return new ResponseEntity<>(new PagamentoChaveDTO(pagamento.getId().toString()), HttpStatus.OK);
    }

    @Operation(summary = "Obter Pagamentos de um cliente", description = "Serviço utilizado para obter pagamento um cliente.")
    @GetMapping(path="/cliente/{chave}", produces = "application/json")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('INTEGRATION')")
    public ResponseEntity<?> getPagamentos(@PathVariable String chave) {
        List<PagamentoResponseDTO> pagamentoReponseDTOList = obtemPagamentosPorCliente.execute(chave);
        return new ResponseEntity<>(pagamentoReponseDTOList, HttpStatus.OK);
    }

}
