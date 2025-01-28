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
    public void updatePassword(Long userId, String newPassword, String oldPassword) {
        Optional<Person> person = personRepository.getById(userId);
        if (person.isEmpty()) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }

        if (!(passwordEncoder.matches(oldPassword, person.get().getPassword()))) {
            throw new CustomException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        if (personRepository.updatePassword(userId, encodedNewPassword) <= 0) {
            throw new CustomException("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PersonDTO showPersonDetails(int hostId) {
        Optional<Person> data = personRepository.getById(Integer.toUnsignedLong(hostId));
        if (data.isEmpty()) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        return converter.personToDetails(data.get());
    }

    @Override
    public PersonDTO createNewUser(PersonDTO newUser) {
        Person model = converter.DTOtoPerson(newUser);
        model.setRole(newUser.getRole());
        model.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Long id = personRepository.save(model);
        if (id == null) {
            throw new CustomException("Failed creating new user", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        newUser.setId(id);
        return newUser;
    }

    @Override
    public void bandUser(Long id) {
        int userId = reportRepository.getReportedUserId(id);
        if (userId == -1) {
            throw new CustomException("User doesn't exist", HttpStatus.NOT_FOUND);
        }
        personRepository.bandUser(userId);
    }
    @Override
    public void unBandUser(Long id){
        // Needs contact with the user to get the ID, so the user always exist
        personRepository.unBandUser(id);
    }
}