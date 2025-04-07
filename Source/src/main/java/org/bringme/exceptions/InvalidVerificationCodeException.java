package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class InvalidVerificationCodeException extends CustomException {
    public InvalidVerificationCodeException(String email) {
        super("Invalid code",
                (String.format("Invalid verification code was inserter by user with email %s", email)),
                Level.INFO,
                HttpStatus.BAD_REQUEST,
                null
        );
    }
}
