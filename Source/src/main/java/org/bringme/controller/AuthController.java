package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.dto.PersonDTO;
import org.bringme.model.AuthLogin;
import org.bringme.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<HashMap<String, Object>> signUp(@Valid @RequestBody PersonDTO requestPerson) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Check if the user exists
        if (authService.isExist(requestPerson.getEmail())) {
            responseMap.put("Message", "Provided credentials used already");
            return new ResponseEntity<>(responseMap, HttpStatus.FORBIDDEN);
        }

        // Save new person in DB
        PersonDTO responsePerson = authService.signUp(requestPerson);
        if (responsePerson == null) {
            responseMap.put("Message", "Unknown error");
            responseMap.put("Person", null);
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        responseMap.put("Message", "Person created successfully.");
        responseMap.put("Person", responsePerson);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(@Valid @RequestBody AuthLogin loginData) {
        HashMap<String, Object> responseMap = new HashMap<>();
        String token = authService.verify(loginData);
        if (token == null) {
            responseMap.put("Message", "Invalid credentials");
            responseMap.put("Token", null);
            return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }
        responseMap.put("Message", "Login done");
        responseMap.put("JWT-Token", token);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
