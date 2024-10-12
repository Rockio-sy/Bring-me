package org.bringme.service;

import org.bringme.dto.PersonDTO;
import org.bringme.model.Person;

import java.util.List;

public interface PersonService {
    Person getPersonById(Long id);
    PersonDTO savePerson(PersonDTO requestPerson);
    PersonDTO getByEmail(String email);
}
