package org.bringme.exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    /**
     * Custom exception to send it as HTTP response by {@link org.bringme.controller.ControllerAdvice Advicer}
     * @param message The content of response
     * @param status HTTP status code.
     */
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
