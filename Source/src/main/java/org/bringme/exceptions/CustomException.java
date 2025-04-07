package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    private final String responseMessage;
    private final String logMessage;
    private final Level logLevel;
    private final HttpStatus status;

    public CustomException(String responseMessage, String logMessage, Level logLevel, HttpStatus status, Throwable cause) {
        super(responseMessage, cause);
        this.responseMessage = responseMessage;
        this.logMessage = logMessage;
        this.logLevel = logLevel;
        this.status = status;

    }


    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public Level getLogLevel() {
        return logLevel;
    }
}
