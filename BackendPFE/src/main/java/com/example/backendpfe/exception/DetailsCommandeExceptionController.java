package com.example.backendpfe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DetailsCommandeExceptionController {
    @ExceptionHandler(value = DetailsCommandeNotfoundException.class)
    public ResponseEntity<Object> exception(DetailsCommandeNotfoundException exception){
        return new ResponseEntity<>("Details Commande not found", HttpStatus.NOT_FOUND);
    }
}
