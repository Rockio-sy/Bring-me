package org.bringme.service.impl;

import org.bringme.dto.PersonDTO;
import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
import org.bringme.service.PersonService;
import org.bringme.utils.Converter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final Converter converter;
    private final BCryptPasswordEncoder passwordEncoder;


    public PersonServiceImpl(PersonRepository personRepository, Converter converter, BCryptPasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PersonDTO getPersonById(Long id) {
        Optional<Person> person = personRepository.getById(id);
        if (person.isEmpty()) {
            return null;
        }
        PersonDTO response = converter.personToDTO(person.get());
        response.setPassword(null);
        return response;
    }

    @Override
    public PersonDTO getByPhone(String phone) {
        Optional<Person> person = personRepository.getByPhone(phone);
        if (person.isEmpty()) {
            return null;
        }
        PersonDTO response = converter.personToDTO(person.get());
        response.setPassword(null);
        return response;
    }


    @Override
    public PersonDTO getByEmail(String email) {
        Optional<Person> person = personRepository.getByEmail(email);
        if (person.isEmpty()) {
            return null;
        }
        PersonDTO response = converter.personToDTO(person.get());
        response.setPassword(null);
        return response;
    }

    @Override
    public int updatePassword(Long userId, String newPassword, String oldPassword) {
        Optional<Person> person = personRepository.getById(userId);
        if (person.isEmpty()) {
            return 3;
        }


        if (!(passwordEncoder.matches(oldPassword, person.get().getPassword()))) {
            return 1;
        }


        String encodedNewPassword = passwordEncoder.encode(newPassword);
        int check = personRepository.updatePassword(userId, encodedNewPassword);
        if (check <= 0) {
            return 2;
        }

        return 0;
    }

}