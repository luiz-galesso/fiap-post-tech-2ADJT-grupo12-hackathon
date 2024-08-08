package com.hackathon.fiap.mscliente.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
public class MessageError {
    private HttpStatus status;
    private List<String> errors;

    public MessageError(HttpStatus status, List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public MessageError(HttpStatus status, String error) {
        super();
        this.status = status;
        errors = Arrays.asList(error);
    }
}
