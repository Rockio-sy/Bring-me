package org.bringme.service.impl;

import org.bringme.dto.PersonDTO;
import org.bringme.model.AuthLogin;
import org.bringme.model.Person;
import org.bringme.repository.AuthRepository;
import org.bringme.service.AuthService;
import org.bringme.utils.Converter;
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

    public AuthServiceImpl(Converter converter, AuthRepository authRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
        this.converter = converter;
        this.authenticationManager = authenticationManager;
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

        // Convert to DTO
        requestPerson.setId(generatedId);
        return requestPerson;
    }

    @Override
    public boolean isExist(String emailOrPhone){
        Optional<Person> person = authRepository.getByEmailOrPhone(emailOrPhone);
        return person.isPresent();
    }

    @Override
    public PersonDTO getByEmailOrPhone(String emailOrPhone){
        Optional<Person> person = authRepository.getByEmailOrPhone(emailOrPhone);
        if(person.isEmpty()){
            return null;
        }
        PersonDTO response = converter.personToDTO(person.get());
        response.setPassword(null);
        return response;
    }

    @Override
    public String verify(AuthLogin loginData){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.emailOrPhone(), loginData.password()));
        if(authentication.isAuthenticated()){
            return "SUCCESS";
        }
        return null;
    }
}
