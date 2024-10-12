package org.bringme.repository;

import org.bringme.model.Person;

import java.util.List;
import java.util.Optional;


public interface PersonRepository {
    Long savePerson(Person person);
    Optional<Person> getById(Long id);
    Optional<Person> getByEmail(String email);
//    Optional<Person> getPersonByPhone(String phone);
//    void deleteById(Long id);
//    int updatePerson(Person person);
//    List<Person> getAll();
}
