package org.bringme.controller;


import org.bringme.service.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class ControllerAdvice {

    // TODO: Use this in all methods for clean code
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<HashMap<String, Object>> customExceptionHandler(CustomException ex){
        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<HashMap<String, Object>> handleGeneralException(Exception ex) {
//        System.out.println(ex.getMessage());
//        HashMap<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        errorResponse.put("message", ex.getMessage());
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
