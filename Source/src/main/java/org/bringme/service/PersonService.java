package org.bringme.service;

import org.bringme.dto.PersonDTO;
import org.bringme.model.Person;

import java.util.List;

public interface PersonService {
    PersonDTO getPersonById(Long id);
    PersonDTO getByEmail(String email);
    PersonDTO getByPhone(String phone);
}
