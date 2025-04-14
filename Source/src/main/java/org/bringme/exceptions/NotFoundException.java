package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(String responseMessage, String operation) {
        super(responseMessage,
                String.format("User tried to get %s data, but was empty", operation),
                Level.WARN,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}
