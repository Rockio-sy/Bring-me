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


    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewUser(@Valid @RequestBody PersonDTO requestPerson) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Check if the user exists
        PersonDTO check = personService.getByEmail(requestPerson.getEmail());
        if(check != null){
            responseMap.put("Message", "Account already exists");
            return new ResponseEntity<>(responseMap, HttpStatus.FORBIDDEN);
        }

        // Save new person in DB
        PersonDTO responsePerson = personService.savePerson(requestPerson);
        if (responsePerson == null) {
            responseMap.put("Message", "Unknown error");
            responseMap.put("Person", null);
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        responseMap.put("Message", "Person created successfully.");
        responseMap.put("Person", responsePerson);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }
}

//Deprecated
//    @PutMapping("/me")
//    public ResponseEntity<Person> updatePerson(@RequestBody Person person){
//        Person updatedPerson = personService.updatePerson(person);
//        if (updatedPerson != null){
//            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//    }
