package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;


public class CannotGetIdOfInsertDataException extends CustomException {
    public CannotGetIdOfInsertDataException(String operation, Throwable cause) {
        super("Unexpected error",
                String.format("Couldn't return id of operation: %s", operation),
                Level.ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                cause
        );
    }
}
