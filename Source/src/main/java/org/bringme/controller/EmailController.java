package org.bringme.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.bringme.repository.PersonRepository;
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
    @PostMapping("/code")
    public ResponseEntity<HashMap<String, Object>> verifyEmail(@NotBlank(message = "Email cannot be empty") @RequestParam("email") String email, @NotBlank(message = "Code required") @RequestParam("code") String userInput) {
        HashMap<String, Object> responseMap = new HashMap<>();
        emailService.validateCode(userInput, email);
        responseMap.put("Message", "Account verified.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/code")
    public ResponseEntity<HashMap<String, Object>> sendVerificationEmail(@Valid @RequestParam("email") String email) {
        HashMap<String, Object> responseMap = new HashMap<>();
        personService.getByEmail(email);
        emailService.sendVerificationCode(email);
        responseMap.put("Message", "Email has been sent successfully.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
