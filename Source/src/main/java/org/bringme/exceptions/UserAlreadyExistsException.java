package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException(String emailOrPhone) {
        super("User already exists",
                String.format("User with credentials %s already exists", emailOrPhone),
                Level.WARN,
                HttpStatus.CONFLICT,
                null
        );
    }
}
