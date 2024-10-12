package org.bringme.service.impl;

import org.bringme.dto.PersonDTO;
import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
import org.bringme.service.PersonService;
import org.bringme.utils.Converter;

import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final Converter converter;
//    private final BCryptPasswordEncoder passwordEncoder;

    public PersonServiceImpl(PersonRepository personRepository, Converter converter) {
//        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.converter = converter;
    }

    @Override
    public Person getPersonById(Long id) {
        Optional<Person> newPerson = personRepository.getById(id);
        return newPerson.orElse(null);
    }

    @Override
    public PersonDTO savePerson(PersonDTO requestPerson) {
        // Convert to model
        Person modelPerson = converter.DTOtoPerson(requestPerson);

        // Save the HASH password
//        String hasPass = passwordEncoder.encode(requestPerson.getPassword());
//        modelPerson.setPassword(hasPass);

        // Get generated ID
        Long generatedId = personRepository.savePerson(modelPerson);

        // Convert to DTO
        PersonDTO response = converter.personToDTO(modelPerson);
        response.setId(generatedId);
        return response;
    }

    @Override
    public PersonDTO getByEmail(String email){
        Optional<Person> person = personRepository.getByEmail(email);
        return person.map(converter::personToDTO).orElse(null);
    }

}