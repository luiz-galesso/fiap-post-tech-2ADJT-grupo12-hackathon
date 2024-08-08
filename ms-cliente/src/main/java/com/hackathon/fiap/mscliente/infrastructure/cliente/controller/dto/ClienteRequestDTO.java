package com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClienteRequestDTO(@JsonProperty("cpf") String cpf
                                ,@JsonProperty("nome") String nome
                                ,@JsonProperty("email") String email
                                ,@JsonProperty("telefone") String telefone
                                ,@JsonProperty("rua") String rua
                                ,@JsonProperty("cidade") String cidade
                                ,@JsonProperty("estado") String estado
                                ,@JsonProperty("cep") String cep
                                ,@JsonProperty("pais") String pais

){
}
