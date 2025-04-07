package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class CannotSendingEmailException extends CustomException {
    public CannotSendingEmailException(Throwable cause, String email, String operation) {
        super("Error with sending an email",
                String.format("Couldn't send verification email to user with email %s while %s", email, operation),
                Level.ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                cause);
    }
}
