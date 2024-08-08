package com.hackathon.fiap.mscliente.infrastructure.cliente.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteRequestDTO(@CPF(message = "CPF nao valido") @JsonProperty("cpf") String cpf
                                ,@JsonProperty("nome") String nome
                                ,@Email(message = "Email nao valido") @JsonProperty("email") String email
                                ,@JsonProperty("telefone") String telefone
                                ,@JsonProperty("rua") String rua
                                ,@JsonProperty("cidade") String cidade
                                ,@JsonProperty("estado") String estado
                                ,@JsonProperty("cep") String cep
                                ,@JsonProperty("pais") String pais

){
}
