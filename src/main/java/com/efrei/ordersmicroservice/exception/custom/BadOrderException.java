package com.efrei.ordersmicroservice.exception.custom;

public class BadOrderException extends RuntimeException{
    public BadOrderException(String message) {
        super(message);
    }
    public BadOrderException(String message, Exception e) {
        super(message, e);
    }

}
