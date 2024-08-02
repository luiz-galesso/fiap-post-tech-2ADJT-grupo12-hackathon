package com.hackathon.fiap.mspagamento.usecase.exception;

public class LimitErrorException extends RuntimeException {
    public LimitErrorException(String message) {
        super(message);
    }
    public LimitErrorException() {

    }
}

