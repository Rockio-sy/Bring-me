package org.bringme.repository;

import org.bringme.model.Person;

import java.util.List;
import java.util.Optional;


public interface PersonRepository {
    List<Person> getAll();
    Optional<Person> getById(Long id);
    int savePerson(Person person);
    int updatePerson(Person person);
    void deleteById(Long id);
    Optional<Person> getPersonByPhone(String phone);
}
