package com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClienteResponseDTO(@JsonProperty("id_cliente") String id_cliente){
}
