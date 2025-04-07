package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class DataBaseException extends CustomException {
    public DataBaseException(Throwable cause) {
        super("Unexpected error occurred",
                "Database error",
                Level.ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                cause);
    }
}
