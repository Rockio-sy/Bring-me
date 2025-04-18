package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class RequestNotApprovedException extends CustomException {
    public RequestNotApprovedException(String message, Long userId){
        super(message,
                "User with id %s tried to get  not approved request",
                Level.INFO,
                HttpStatus.FORBIDDEN,
                null);
    }
}
