package org.bringme.repository;

import org.bringme.model.Person;

import java.util.List;
import java.util.Optional;


public interface PersonRepository {
    Optional<Person> getById(Long id);
    Optional<Person> getByEmail(String email);
    Optional<Person> getByPhone(String phone);

    int updatePassword(Long userId, String encodedNewPassword);

    void verifyAccount(Long id);

    Long getIdByEmailOrPhone(String emailOrPhone);

    Long save(Person model);
}
