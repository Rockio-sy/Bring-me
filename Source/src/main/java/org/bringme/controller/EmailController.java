package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.repository.PersonRepository;
import org.bringme.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.Phaser;

@RestController
@RequestMapping("/bring-me/ver")
public class EmailController {
    private final EmailService emailService;
    private final PersonRepository personRepository;
    public EmailController(EmailService emailService, PersonRepository personRepository) {
        this.emailService = emailService;
        this.personRepository = personRepository;
    }

    @PostMapping("/code") // Check the line number in things_to_discuss
    public ResponseEntity<HashMap<String, Object>> verifyEmail(@Valid @RequestParam("email") String email, @Valid @RequestParam("code") String userInput) {
        HashMap<String, Object> responseMap = new HashMap<>();

        if (email.isEmpty()) {
            responseMap.put("Message", "Email cannot be null");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        int check = emailService.validateCode(userInput, email);
        if (check == 1) {
            responseMap.put("Message", "Invalid code");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }else if (check == 2){
            responseMap.put("Message", "Account doesn't exist");
            return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }

        responseMap.put("Message", "Account verified.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/code")
    public ResponseEntity<HashMap<String, Object>> sendVerificationEmail(@Valid @RequestParam("email") String email) {
        HashMap<String, Object> responseMap = new HashMap<>();

        if(personRepository.getIdByEmailOrPhone(email) == null){
            responseMap.put("Message", "No account for given email");
            return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        }
        String code = emailService.sendVerificationCode(email);
        responseMap.put("Message", "Email has been sent successfully.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
