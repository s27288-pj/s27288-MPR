package org.bank.s27288_bank.exception;

import lombok.Getter;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private String message;
    @Getter
    private String field;
    @Getter
    private Map<String, String> errors;

    public ValidationException(String message, String field) {
        super(message);
        this.message = message;
        this.field = field;
    }

    @Override
    public String getMessage() {
        return field + " - " + message;
    }
}
