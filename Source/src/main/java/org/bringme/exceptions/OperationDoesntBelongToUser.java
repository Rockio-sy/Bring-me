package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class OperationDoesntBelongToUser extends CustomException{
    public OperationDoesntBelongToUser(String message, Long userId) {
        super(message,
                String.format("User with id=%s tried to get something doesn't belong to them", userId),
                Level.WARN,
                HttpStatus.FORBIDDEN,
                null
        );
    }
}
