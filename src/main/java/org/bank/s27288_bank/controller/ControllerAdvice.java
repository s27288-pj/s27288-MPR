package org.bank.s27288_bank.controller;

import org.bank.s27288_bank.exception.ValidationException;
import org.bank.s27288_bank.model.request.ErrorResponse;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        if (e.getErrors() != null && !e.getErrors().isEmpty()) {
            errorResponse = new ErrorResponse("Validation failed", e.getErrors());
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
