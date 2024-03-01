package com.efrei.ordersmicroservice.exception.custom;

public class CatalogMicroserviceException extends RuntimeException{
    public CatalogMicroserviceException(String message, Exception e) {
        super(message, e);
    }
}
