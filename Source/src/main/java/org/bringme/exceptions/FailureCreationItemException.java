package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class FailureCreationItemException extends CustomException {
    public FailureCreationItemException(String responseMessage, Throwable cause, String operation) {
        super(responseMessage,
                String.format("Cannot work with a file (read or write) in operation %s", operation),
                Level.ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                cause
        );
    }
}
