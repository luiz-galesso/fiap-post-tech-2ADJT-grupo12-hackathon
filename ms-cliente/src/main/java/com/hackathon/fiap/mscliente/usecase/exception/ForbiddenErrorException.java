package com.hackathon.fiap.mscliente.usecase.exception;

public class ForbiddenErrorException extends RuntimeException {
    public ForbiddenErrorException(String message) {
        super(message);
    }
    public ForbiddenErrorException() {

    }
}

