package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class LockedAccountException extends CustomException {
    public LockedAccountException(String emailOfPhone) {
        super("Account is locked",
                String.format("Locked account tried to login with email or phone %s", emailOfPhone),
                Level.WARN,
                HttpStatus.FORBIDDEN,
                null
        );
    }
}
