package com.hackathon.fiap.mscliente.infrastructure.exception;

import com.hackathon.fiap.mscliente.usecase.exception.BusinessErrorException;
import com.hackathon.fiap.mscliente.usecase.exception.EntityNotFoundException;
import com.hackathon.fiap.mscliente.usecase.exception.ForbiddenErrorException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {
    private ErrorDefaultResponse errorReponse = new ErrorDefaultResponse();

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDefaultResponse> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        errorReponse.setTimestamp(Instant.now());
        errorReponse.setStatus("KO");
        errorReponse.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(this.errorReponse);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDefaultResponse> invalidRequest(HttpMessageNotReadableException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorReponse.setTimestamp(Instant.now());
        errorReponse.setStatus("KO");
        errorReponse.setMessage("Requisição inválida.");
        return ResponseEntity.status(status).body(this.errorReponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDefaultResponse> generic(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        errorReponse.setTimestamp(Instant.now());
        errorReponse.setStatus("KO");
        errorReponse.setMessage("Ocorreu um erro genérico na aplicação.");
        return ResponseEntity.status(status).body(this.errorReponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageError> argumentError(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        errorReponse.setTimestamp(Instant.now());
        errorReponse.setStatus("KO");
        MessageError apiErrorMessage = new MessageError(status, errors);
        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }


    @ExceptionHandler(BusinessErrorException.class)
    public ResponseEntity<ErrorDefaultResponse> bussinessError(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        errorReponse.setTimestamp(Instant.now());
        errorReponse.setStatus("KO");
        errorReponse.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(this.errorReponse);
    }

    @ExceptionHandler(ForbiddenErrorException.class)
    public ResponseEntity<ErrorDefaultResponse> forbiddenError(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        errorReponse.setTimestamp(Instant.now());
        errorReponse.setStatus("KO");
        errorReponse.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(this.errorReponse);
    }
}