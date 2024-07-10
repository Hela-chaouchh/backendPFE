package com.example.backendpfe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommandeExceptionController {
    @ExceptionHandler(value = CommandeNotfoundException.class)
    public ResponseEntity<Object> exception(CommandeNotfoundException exception){
        return new ResponseEntity<>("Commande not found", HttpStatus.NOT_FOUND);
    }
}
