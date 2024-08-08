package com.hackathon.fiap.mscliente.usecase.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException() {

    }
}

