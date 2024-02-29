package com.efrei.ordersmicroservice.exception;

import com.efrei.ordersmicroservice.exception.custom.JWTException;
import com.efrei.ordersmicroservice.exception.custom.OrderNotFoundException;
import com.efrei.ordersmicroservice.exception.custom.WrongUserRoleException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JWTException.class)
    protected ResponseEntity<Object> handleJWTException(
            JWTException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage() + "\n" + ex.getCause().toString();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(WrongUserRoleException.class)
    protected ResponseEntity<Object> handleWrongUserRole(
            WrongUserRoleException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(
            OrderNotFoundException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
