package org.bringme.service;

import org.bringme.model.Person;

import java.util.List;

public interface PersonService {
    List<Person> getAllPersons();
    Person getPersonById(Long id);
    Person savePerson(Person person);
    Person updatePerson(Person person);
    void deletePerson(Long id);
    Person getPersonByPhone(String phone);
}
