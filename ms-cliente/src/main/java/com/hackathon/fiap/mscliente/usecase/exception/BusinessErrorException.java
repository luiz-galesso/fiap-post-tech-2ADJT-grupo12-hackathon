package com.hackathon.fiap.mscliente.usecase.exception;

public class BusinessErrorException extends RuntimeException {
    public BusinessErrorException(String message) {
        super(message);
    }
    public BusinessErrorException() {

    }
}

