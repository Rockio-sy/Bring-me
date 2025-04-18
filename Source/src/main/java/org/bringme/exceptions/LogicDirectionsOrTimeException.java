package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class LogicDirectionsOrTimeException extends CustomException {
    public LogicDirectionsOrTimeException(String message) {
        super(message,
                String.format("User tried to use trip with incompatible directions or unlogical time {%s}", message),
                Level.INFO,
                HttpStatus.BAD_REQUEST,
                null
        );
    }
}
