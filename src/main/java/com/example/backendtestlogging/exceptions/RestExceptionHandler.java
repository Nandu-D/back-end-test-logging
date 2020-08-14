package com.example.backendtestlogging.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MissingCrucialLogInfoException.class)
    private ResponseEntity<String> missingCrucialLogInfoException(MissingCrucialLogInfoException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ArgumentParsingException.class)
    private ResponseEntity<String> argumentParsingException(ArgumentParsingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
