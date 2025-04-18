package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class PasswordSetUpException extends CustomException {
    public PasswordSetUpException(String message) {
        super(message,
                String.format("User tried to set up the password, with error %s", message),
                Level.WARN,
                HttpStatus.BAD_REQUEST
        ,null);
    }
}
