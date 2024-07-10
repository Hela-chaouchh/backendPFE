package com.example.backendpfe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ClientExceptionController {
    @ExceptionHandler(value = ClientNotfoundException.class)
    public ResponseEntity<Object> exception(ClientNotfoundException exception){
        return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
    }
}

