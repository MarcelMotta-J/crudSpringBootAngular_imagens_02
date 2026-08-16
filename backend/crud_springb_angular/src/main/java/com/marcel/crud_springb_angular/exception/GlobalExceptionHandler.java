package com.marcel.crud_springb_angular.exception;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException exception) {

        Map<String, Object> error = new HashMap<>();

        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Validation error");

        String message = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Invalid data");

        error.put("message", message);
        error.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Data conflict");
        error.put("message", "CPF or email already registered");
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
