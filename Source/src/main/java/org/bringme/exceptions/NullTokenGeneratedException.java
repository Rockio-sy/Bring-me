package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class NullTokenGeneratedException extends CustomException {
    public NullTokenGeneratedException(Long userId) {
        super("Invalid credentials",
                String.format("Couldn't generate token for user with id = %s", userId),
                Level.WARN,
                HttpStatus.UNAUTHORIZED,
                null
        );
    }
}
