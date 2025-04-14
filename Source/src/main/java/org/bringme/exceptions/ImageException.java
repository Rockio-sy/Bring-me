package org.bringme.exceptions;


import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class ImageException extends CustomException{
    public ImageException(String responseMessage, String logMessage, Level level, HttpStatus status, Throwable cause){
        super(
                responseMessage,
                logMessage,
                level,
                status,
                cause
        );
    }
}
