package org.bringme.service.exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
