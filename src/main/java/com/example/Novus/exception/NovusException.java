package com.example.Novus.exception;

public class NovusException extends RuntimeException {
    public NovusException(String message) {
        super(message);
    }

    public NovusException(String message, Throwable cause) {
        super(message, cause);
    }
}