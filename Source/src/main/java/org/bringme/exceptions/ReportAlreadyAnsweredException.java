package org.bringme.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class ReportAlreadyAnsweredException extends CustomException{
    public ReportAlreadyAnsweredException() {
        super("Report Already answered",
                "User tried to get answered report",
                Level.INFO,
                HttpStatus.CONFLICT,
                null);
    }
}
