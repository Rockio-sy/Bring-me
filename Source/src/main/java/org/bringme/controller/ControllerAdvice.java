package org.bringme.controller;


import org.bringme.service.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<HashMap<String, Object>> customExceptionHandler(CustomException ex){
        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<HashMap<String, Object>> handleGeneralException(Exception ex) {
        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred.");

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
