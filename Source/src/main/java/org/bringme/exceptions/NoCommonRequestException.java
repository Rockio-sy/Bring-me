package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class NoCommonRequestException extends CustomException{
    public NoCommonRequestException(Long firstId, int secondId, Throwable cause){
        super(
                "No common request with chosen user",
                String.format("User with id=%s tried to check user's data with id=%s", firstId, secondId),
                Level.WARN,
                HttpStatus.BAD_REQUEST,
                null
        );
    }
}
