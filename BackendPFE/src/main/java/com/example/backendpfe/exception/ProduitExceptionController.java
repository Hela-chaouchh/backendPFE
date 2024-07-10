package com.example.backendpfe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProduitExceptionController {
    @ExceptionHandler(value = ProduitNotfoundException.class)
    public ResponseEntity<Object> exception(ProduitNotfoundException exception){
        return new ResponseEntity<>("Produit not found", HttpStatus.NOT_FOUND);
    }
}
