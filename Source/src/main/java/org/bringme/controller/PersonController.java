package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.dto.PersonDTO;
import org.bringme.model.Person;
import org.bringme.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("bring-me/u/")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }
    // Update password endpoint to be implemented
}
