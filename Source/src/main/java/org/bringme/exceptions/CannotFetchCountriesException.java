package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class CannotFetchCountriesException extends CustomException {
    public CannotFetchCountriesException(Throwable cause) {
        super("Cannot fetch countries",
                "Couldn't fetch countries",
                Level.ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                cause);
    }
}