package org.bringme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.bringme.dto.AuthLogin;
import org.bringme.dto.PersonDTO;
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


    @Operation(
            summary = "Sign up a new user",
            description = "Creates a new user by validating email and phone, and saving their details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
            @ApiResponse(responseCode = "409", description = "Conflict - Email or phone already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<HashMap<String, Object>> signUp(@RequestBody @Valid PersonDTO requestPerson) {
        HashMap<String, Object> responseMap = new HashMap<>();
        authService.checkEmailAndPhone(requestPerson.getEmail(), requestPerson.getPhone());
        PersonDTO responsePerson = authService.signUp(requestPerson);
        responseMap.put("Message", "Person created successfully.");
        responseMap.put("Person", responsePerson);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }
    @Operation(
            summary = "Login",
            description = "Login by email or phone"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person logged in",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
    })
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
