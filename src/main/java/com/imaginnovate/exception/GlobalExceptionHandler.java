package com.imaginnovate.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        // Create a custom response object to hold validation error details
        Map<String, String> errors = new HashMap<>();

        // Populate the response object with validation error details
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        // Return ResponseEntity with the custom response object and appropriate HTTP status
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
