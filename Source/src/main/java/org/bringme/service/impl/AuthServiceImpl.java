package org.bringme.service.impl;

import org.bringme.dto.PersonDTO;
import org.bringme.dto.AuthLogin;
import org.bringme.model.Person;
import org.bringme.repository.AuthRepository;
import org.bringme.service.AuthService;
import org.bringme.service.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final Converter converter;
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(Converter converter, AuthRepository authRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
        this.converter = converter;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @Override
    public PersonDTO signUp(PersonDTO requestPerson) {
        // Convert to model
        Person modelPerson = converter.DTOtoPerson(requestPerson);

        // Save the HASH password
        String hashPass = passwordEncoder.encode(requestPerson.getPassword());
        modelPerson.setPassword(hashPass);

        // Get generated ID
        Long generatedId = authRepository.savePerson(modelPerson);
        if (generatedId == null) {
            throw new CustomException("Problem while creating the account", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Convert to DTO
        requestPerson.setId(generatedId);
        requestPerson.setAccountStatus(1);
        return requestPerson;
    }

    // Specially for authentication!!
    @Override
    public void isExist(String emailOrPhone) {
        Optional<Person> person = authRepository.getByEmailOrPhone(emailOrPhone);
        if (person.isPresent()) {
            throw new CustomException("User already exist(used resources)", HttpStatus.CONFLICT);
        }
    }

    @Override
    public PersonDTO getByEmailOrPhone(String emailOrPhone) {
        Optional<Person> person = authRepository.getByEmailOrPhone(emailOrPhone);
        if (person.isEmpty()) {
            return null;
        }
        PersonDTO response = converter.personToDTO(person.get());
        response.setPassword(null);
        return response;
    }

    @Override
    public String generateToken(AuthLogin loginData) {
        String token = null;
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.emailOrPhone(), loginData.password()));
        if (authentication.isAuthenticated()) {
            Optional<Person> personToInclude = authRepository.getByEmailOrPhone(loginData.emailOrPhone());
            if (personToInclude.isEmpty()) {
                throw new CustomException("User not found", HttpStatus.NOT_FOUND);
            }
            token = jwtService.generateToken(personToInclude.get(), loginData.emailOrPhone());

        }
        if (token == null) {
            throw new CustomException("Invalid credentials", HttpStatus.FORBIDDEN);
        }
        return token;
    }

    @Override
    public void isValidated(AuthLogin loginData) {
        if (!authRepository.isVerified(loginData.emailOrPhone())) {
            throw new CustomException("Email verification needed", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkEmailAndPhone(String email, String phone) {
        if(authRepository.getByEmailOrPhone(email).isPresent()){
            throw new CustomException("User already existed", HttpStatus.CONFLICT);
        }
        if(authRepository.getByEmailOrPhone(phone).isPresent()){
            throw new CustomException("User already existed", HttpStatus.CONFLICT);
        }
    }
}
