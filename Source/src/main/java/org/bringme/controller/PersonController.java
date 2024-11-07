package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.service.PersonService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

@RestController
@RequestMapping("bring-me/u/")
public class PersonController {
    private final PersonService personService;
    private final JwtService jwtService;

    public PersonController(PersonService personService, JwtService jwtService) {
        this.personService = personService;
        this.jwtService = jwtService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<HashMap<String, Object>> updatePassword(@RequestHeader(value = "Authorization") String header,
                                                                  @Valid @RequestParam(name = "new") String newPassword,
                                                                  @Valid @RequestParam(name = "old") String oldPassword) {

        HashMap<String, Object> responseMap = new HashMap<>();
        if (header == null) {
            responseMap.put("Message", "Invalid token.");
            return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }

        // Ask for the user id and for the way of checking password in if(1, 2)
        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);
        int check = personService.updatePassword(userId, newPassword, oldPassword);
        if (check == 1) {
            responseMap.put("Message", "Invalid old password.");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        } else if (check == 2) {
            responseMap.put("Message", "Internal server error");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (check == 3) {
            responseMap.put("Message", "User not found");
            return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        }

        responseMap.put("Message", "Password had been updated");
        responseMap.put("New password", newPassword);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
