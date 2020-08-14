package com.example.backendtestlogging.exceptions;

public class ArgumentParsingException extends RuntimeException {
    public ArgumentParsingException(String message) {
        super(message);
    }
}
