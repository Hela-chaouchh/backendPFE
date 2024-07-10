package com.example.backendpfe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ConfigStoreExceptionController {
    @ExceptionHandler(value = ConfigStoreNotfoundException.class)
    public ResponseEntity<Object> exception(ConfigStoreNotfoundException exception){
        return new ResponseEntity<>("ConfigStore not found", HttpStatus.NOT_FOUND);
    }
}
