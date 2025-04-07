package org.bringme.controller;


import org.bringme.exceptions.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

/**
 * Controller advice:
 * handles thrown exceptions from the server layer then sends them as HTTP response.
 */
@RestControllerAdvice
public class ControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    /**
     * Specified for {@link  CustomException}
     *
     * @param ex Handled {@link  CustomException}
     * @return http response {@link ResponseEntity}
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> customExceptionHandler(CustomException ex) {
        switch (ex.getLogLevel()) {
            case ERROR -> logger.error(ex.getLogMessage(), ex.getCause());
            case WARN -> logger.error(ex.getLogMessage());
            case INFO -> logger.info(ex.getLogMessage());
            default -> logger.info(ex.getResponseMessage());
        }
        return new ResponseEntity<>(ex.getResponseMessage(), ex.getStatus());
    }

    /**
     * Specified for {@link jakarta.validation.Valid}  annotation
     *
     * @param ex Handled {@link org.springframework.web.bind.MethodArgumentNotValidException}
     * @return Http response {@link ResponseEntity}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        HashMap<String, Object> errorResponse = new HashMap<>();

        // Extract specific validation errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        errorResponse.put("Message", "Validation failed");
        errorResponse.put("Errors", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Specified for any exception not included in other handlers
     *
     * @param ex Handled {@link java.lang.Exception}
     * @return Http response {@link ResponseEntity}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HashMap<String, Object>> handleGeneralException(Exception ex) {
        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleAuthException(UsernameNotFoundException ex) {
        logger.warn("Authentication error occurred", ex.getCause());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
