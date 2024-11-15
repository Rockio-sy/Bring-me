package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.dto.PersonDTO;
import org.bringme.dto.AuthLogin;
import org.bringme.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/bring-me/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<HashMap<String, Object>> signUp(@RequestBody PersonDTO requestPerson) {
        HashMap<String, Object> responseMap = new HashMap<>();
        authService.checkEmailAndPhone(requestPerson.getEmail(), requestPerson.getPhone());
        PersonDTO responsePerson = authService.signUp(requestPerson);
        responseMap.put("Message", "Person created successfully.");
        responseMap.put("Person", responsePerson);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody AuthLogin loginData) {
        HashMap<String, Object> responseMap = new HashMap<>();

        String token = authService.generateToken(loginData);

        // Check if the account is Email-verified
        authService.isValidated(loginData);

        responseMap.put("Message", "Login done");
        responseMap.put("JWT-Token", token);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
