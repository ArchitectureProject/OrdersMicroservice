package com.efrei.ordersmicroservice.exception.custom;

public class JWTException extends RuntimeException {
    public JWTException(String message) {
        super(message);
    }
    public JWTException(String message, Exception e) {
        super(message, e);
    }
}