package com.example.backendtestlogging.exceptions;

public class MissingCrucialLogInfoException extends RuntimeException {
    public MissingCrucialLogInfoException(String message) {
        super(message);
    }
}
