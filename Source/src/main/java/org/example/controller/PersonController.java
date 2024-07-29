package org.example.controller;

import org.example.model.Person;
import org.example.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService){
        this.personService = personService;
    }

//    List<Person> getAllPersons();
//    Person getPersonById(Long id);
//    Person savePerson(Person person);
//    Person updatePerson(Person person);
//    void deletePerson(Long id);
    @GetMapping("/all")
    public ResponseEntity<List<Person>> listAll(){
        List<Person> persons = personService.getAllPersons();
        if(persons.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(persons, HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Person> createNewUser(@RequestBody Person person){
        Person newPerson = personService.savePerson(person);
        if(newPerson != null){
            newPerson = personService.getPersonByPhone(newPerson.getPhone());
            return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    // Should i change /me ?? what to set (update||account), cus no one will change the DB status!
    // What if user wants to change their address or phone?
    // What is the best practice to fetch user if i do not have their ID ?
    @PutMapping("/me")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person){
        Person updatedPerson = personService.updatePerson(person);
        if (updatedPerson != null){
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    // Should i add a condition to tell that user doesn't exist ?
    @DeleteMapping("/delete")
    public void deletePerson(@RequestParam Long id){
        personService.deletePerson(id);
    }
}
