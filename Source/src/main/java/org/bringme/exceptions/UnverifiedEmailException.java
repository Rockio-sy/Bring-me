package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class UnverifiedEmailException extends CustomException {
    public UnverifiedEmailException(String emailOrPhone) {
        super("Email verification needed",
                String.format("User with login data {%s} need to verify email", emailOrPhone),
                Level.WARN,
                HttpStatus.FORBIDDEN,
                null
        );
    }
}
