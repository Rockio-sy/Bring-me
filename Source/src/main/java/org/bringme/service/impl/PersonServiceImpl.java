package org.bringme.service.impl;

import org.bringme.dto.PersonDTO;
import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
import org.bringme.repository.ReportRepository;
import org.bringme.service.PersonService;
import org.bringme.service.exceptions.CustomException;
import org.bringme.utils.Converter;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final Converter converter;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ReportRepository reportRepository;


    public PersonServiceImpl(PersonRepository personRepository, Converter converter, BCryptPasswordEncoder passwordEncoder, ReportRepository reportRepository) {
        this.personRepository = personRepository;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
        this.reportRepository = reportRepository;
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
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
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

    @Override
    public PersonDTO showPersonDetails(int hostId) {
        Optional<Person> data = personRepository.getById(Integer.toUnsignedLong(hostId));
        return data.map(converter::personToDetails).orElse(null);
    }

    @Override
    public PersonDTO createNewUser(PersonDTO newUser) {
        Person model = converter.DTOtoPerson(newUser);
        model.setRole(newUser.getRole());
        model.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Long id = personRepository.save(model);

        newUser.setId(id);
        return newUser;
    }

    @Override
    public void bandUser(Long id) {
        int userId = reportRepository.getReportedUserId(id);
        if(userId == -1){
            throw new CustomException("User doesn't exist", HttpStatus.NOT_FOUND);
        }
        personRepository.bandUser(userId);
    }
}