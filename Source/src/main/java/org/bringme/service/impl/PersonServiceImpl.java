package org.bringme.service.impl;

import org.bringme.dto.PersonDTO;
import org.bringme.exceptions.DataBaseException;
import org.bringme.exceptions.NotFoundException;
import org.bringme.exceptions.PasswordSetUpException;
import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
import org.bringme.repository.ReportRepository;
import org.bringme.service.PersonService;
import org.bringme.exceptions.CustomException;
import org.bringme.utils.Converter;

import org.springframework.dao.EmptyResultDataAccessException;
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

    /**
     * Retrieves a person by their ID and returns a PersonDTO object.
     *
     * @param id The ID of the person to retrieve.
     * @return A PersonDTO object representing the person, or null if no person is found.
     */
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

    /**
     * Retrieves a person by their phone number and returns a PersonDTO object.
     *
     * @param phone The phone number of the person to retrieve.
     * @return A PersonDTO object representing the person, or null if no person is found.
     */
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

    /**
     * Retrieves a person by their email address and returns a PersonDTO object.
     *
     * @param email The email address of the person to retrieve.
     * @return A PersonDTO object representing the person.
     * @throws CustomException If the person is not found, throws a NOT_FOUND exception.
     */
    @Override
    public PersonDTO getByEmail(String email) {
        Optional<Person> person = personRepository.getByEmail(email);
        if (person.isEmpty()) {
            throw new NotFoundException("User not found", "PersonService::getByEmail");
        }
        PersonDTO response = converter.personToDTO(person.get());
        response.setPassword(null);
        return response;
    }

    /**
     * Updates the password for a person.
     *
     * @param userId      The ID of the user whose password is being updated.
     * @param newPassword The new password to set.
     * @param oldPassword The old password to verify.
     * @throws CustomException If the user is not found, if the old password is incorrect,
     *                         or if there is an internal server error when updating the password.
     */
    @Override
    public void updatePassword(Long userId, String newPassword, String oldPassword) {
        Optional<Person> person = personRepository.getById(userId);
        if (person.isEmpty()) {
            throw new NotFoundException("User not found", "PersonService::updatePassword");
        }

        if (!(passwordEncoder.matches(oldPassword, person.get().getPassword()))) {
            throw new PasswordSetUpException("Old password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        try {
            if (personRepository.updatePassword(userId, encodedNewPassword) <= 0) {
                throw new PasswordSetUpException("Cannot update password");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new DataBaseException(e);
        }
    }

    /**
     * Displays the details of a person by their host ID.
     *
     * @param hostId The ID of the host whose details are to be retrieved.
     * @return A PersonDTO object representing the person's details.
     * @throws CustomException If the user is not found, throws a NOT_FOUND exception.
     */
    @Override
    public PersonDTO showPersonDetails(int hostId) {
        Optional<Person> data = personRepository.getById(Integer.toUnsignedLong(hostId));
        if (data.isEmpty()) {
            throw new NotFoundException("User not found", "PersonServiceImp::ShowPersonDetails");
        }
        return converter.personToDetails(data.get());
    }

    /**
     * Creates a new user and saves them to the database.
     *
     * @param newUser A PersonDTO object containing the details of the new user.
     * @return A PersonDTO object representing the created user, with the generated ID.
     * @throws CustomException If there is an error while creating the new user, throws an INTERNAL_SERVER_ERROR exception.
     */
    @Override
    public PersonDTO createNewUser(PersonDTO newUser) {
        Person model = converter.DTOtoPerson(newUser);
        model.setRole(newUser.getRole());
        model.setPassword(passwordEncoder.encode(newUser.getPassword()));

        try {
            Long id = personRepository.save(model);

            newUser.setId(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataBaseException(e);
        }
        return newUser;
    }

    /**
     * Bans a user by their ID.
     *
     * @param id The ID of the user to ban.
     * @throws CustomException If the user does not exist, throws a NOT_FOUND exception.
     */
    @Override
    public void bandUser(Long id) {
        int userId = reportRepository.getReportedUserId(id);
        if (userId == -1) {
            throw new NotFoundException("User doesn't exist", "PersonServiceImpl::bandUser");
        }
        personRepository.bandUser(userId);
    }

    /**
     * Unbans a user by their ID.
     *
     * @param id The ID of the user to unban.
     */
    @Override
    public void unBandUser(Long id) {
        // Needs contact with the user to get the ID, so the user always exist
        personRepository.unBandUser(id);
    }
}