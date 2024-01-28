package org.bank.s27288_bank.exception;

import lombok.Getter;

import java.util.Map;

public class NullValidationException extends NullPointerException {
    private String message;
    @Getter
    private String field;
    @Getter
    private Map<String, String> errors;

    public NullValidationException(String message, String field) {
        super(message);
        this.message = message;
        this.field = field;
    }

    public NullValidationException(String message, Map<String, String> errors) {
        super(message);
        this.message = message;
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return field + ", " + message;
    }

}

