package com.hackathon.fiap.mscliente.usecase.exception;

public class BussinessErrorException extends RuntimeException {
    public BussinessErrorException(String message) {
        super(message);
    }
    public BussinessErrorException() {

    }
}

