package org.bringme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.bringme.service.EmailService;
import org.bringme.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/bring-me/ver")
public class EmailController {
    private final EmailService emailService;
    private final PersonService personService;

    public EmailController(EmailService emailService, PersonService personService) {
        this.emailService = emailService;
        this.personService = personService;
    }

    @Operation(summary = "Get verification code", description = "Verification code consists of 6 digits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/code")
    public ResponseEntity<HashMap<String, Object>> sendVerificationEmail(@Valid @RequestParam("email") String email) {
        HashMap<String, Object> responseMap = new HashMap<>();
        // Throw exception if user not found.
        personService.getByEmail(email);
        emailService.sendVerificationCode(email);
        responseMap.put("Message", "Email has been sent successfully.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Enter verification code", description = "Verification code consists of 6 digits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Verification passed", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad request, code is not correct")
    })
    @PostMapping("/code")
    public ResponseEntity<HashMap<String, Object>> verifyEmail(@Email @RequestParam("email") String email,
                                                               @NotBlank(message = "Code required") @RequestParam("code") String userInput) {
        HashMap<String, Object> responseMap = new HashMap<>();
        emailService.validateCode(userInput, email);
        responseMap.put("Message", "Account verified.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
