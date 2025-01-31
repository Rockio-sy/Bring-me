package org.bringme.repository;

import org.bringme.model.Person;

import java.util.Optional;

public interface AuthRepository {

    Optional<Person> getByEmailOrPhone(String emailOrPhone);
    Long savePerson(Person person);
    Long getIdByEmailOrPhone(String emailOrPhone);

    boolean isVerified(String s);
}
