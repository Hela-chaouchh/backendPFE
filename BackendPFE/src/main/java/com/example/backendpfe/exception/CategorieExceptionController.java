package com.example.backendpfe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CategorieExceptionController {
    @ExceptionHandler(value = CategorieNotfoundException.class)
    public ResponseEntity<Object> exception(CategorieNotfoundException exception){
        return new ResponseEntity<>("Categorie not found", HttpStatus.NOT_FOUND);
    }
}
