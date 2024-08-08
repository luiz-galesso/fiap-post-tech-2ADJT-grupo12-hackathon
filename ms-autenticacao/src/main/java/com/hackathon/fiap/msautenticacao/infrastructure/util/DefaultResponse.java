package com.hackathon.fiap.msautenticacao.infrastructure.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultResponse {
    private Instant timestamp;
    private String status;
    private  String message;
}
