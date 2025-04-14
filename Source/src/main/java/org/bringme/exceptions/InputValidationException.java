package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class InputValidationException extends CustomException {
    public InputValidationException(String responseMessage) {
        super(responseMessage,
                "Input user error"
                , Level.INFO,
                HttpStatus.BAD_REQUEST,
                null);
    }
}
