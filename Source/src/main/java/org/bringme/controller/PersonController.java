package org.bringme.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.bringme.dto.PersonDTO;
import org.bringme.service.PersonService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

@RestController
@RequestMapping("bring-me/p/")
public class PersonController {
    private final PersonService personService;
    private final JwtService jwtService;

    public PersonController(PersonService personService, JwtService jwtService) {
        this.personService = personService;
        this.jwtService = jwtService;
    }

    @PutMapping("/change-password")
    public ResponseEntity<HashMap<String, Object>> updatePassword(@NotBlank @RequestHeader(value = "Authorization") String header,
                                                                  @NotBlank @RequestParam(name = "new") String newPassword,
                                                                  @NotBlank @RequestParam(name = "old") String oldPassword) {
        HashMap<String, Object> responseMap = new HashMap<>();

        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        personService.updatePassword(userId, newPassword, oldPassword);

        responseMap.put("Message", "Password had been updated");
        responseMap.put("New password", newPassword);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("/a/new-user")
    public ResponseEntity<HashMap<String, Object>> createNewUserByAdmin(@RequestHeader(value = "Authorization") String header,@Valid @RequestBody PersonDTO newUser){
        HashMap<String, Object> response = new HashMap<>();
        PersonDTO dto = personService.createNewUser(newUser);
        response.put("Message", "New person has been created.");
        response.put("Person", dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/a/band")
    public ResponseEntity<HashMap<String, Object>> bandUser(@Valid @RequestParam("reportId") Long reportId){
        HashMap<String, Object> responseMap = new HashMap<>();
        personService.bandUser(reportId);
        responseMap.put("Message", "User banned successfully.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
