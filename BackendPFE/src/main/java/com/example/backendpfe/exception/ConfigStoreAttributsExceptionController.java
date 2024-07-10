package com.example.backendpfe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ConfigStoreAttributsExceptionController {
    @ExceptionHandler(value = ConfigStoreAttributsNotfoundException.class)
    public ResponseEntity<Object> exception(ConfigStoreAttributsNotfoundException exception){
        return new ResponseEntity<>("ConfigStoreAttributs not found", HttpStatus.NOT_FOUND);
    }
}
