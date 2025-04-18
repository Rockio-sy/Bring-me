package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class AlreadyExistedException extends CustomException {
    public AlreadyExistedException(String message, String thing) {
        super(message,
                String.format("User tried to save %s, but already existed", thing),
                Level.INFO,
                HttpStatus.CONFLICT,
                null);
    }
}
