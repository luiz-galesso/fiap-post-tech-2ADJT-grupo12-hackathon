package com.hackathon.fiap.mspagamento.infrastructure.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorReponseDTO {
   private Integer status;
   private String message;
}
