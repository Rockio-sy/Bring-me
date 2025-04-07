package org.bringme.service.impl;

import org.bringme.dto.AuthLogin;
import org.bringme.dto.PersonDTO;
import org.bringme.exceptions.*;
import org.bringme.model.Person;
import org.bringme.repository.AuthRepository;
import org.bringme.service.AuthService;
import org.bringme.utils.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    /**
     * Registers a new user in the system by converting the {@link PersonDTO} to a {@link Person} model,
     * hashing the user's password, saving the user in the database, and then returning a {@link PersonDTO}
     * with the generated ID and account status.
     *
     * @param requestPerson The {@link PersonDTO} object containing user data, including email/phone and password.
     * @return {@link PersonDTO} The {@link PersonDTO} object with the generated ID and account status (set to 1).
     * @throws CustomException If an error occurs during account creation (e.g., database save failure),
     *                         an exception with HTTP status {@code 500 INTERNAL_SERVER_ERROR} is thrown.
     */
    @Override
    public PersonDTO signUp(PersonDTO requestPerson) {
        // Convert to model
        Person modelPerson = converter.DTOtoPerson(requestPerson);

        // Save the HASH password
        String hashPass = passwordEncoder.encode(requestPerson.getPassword());
        modelPerson.setPassword(hashPass);

        // Get generated ID
        try {
            Long generatedId = authRepository.savePerson(modelPerson);
            requestPerson.setId(generatedId);
            requestPerson.setAccountStatus(1);
            return requestPerson;
        } catch (DataAccessException e) {
            throw new CannotGetIdOfInsertDataException("SignUp", e);
        }
    }

    /**
     * Checks if a user already exists in the system based on their email or phone number.
     * If the user exists, throws a conflict error.
     *
     * @param emailOrPhone The email or phone number to check for the existence of the user.
     * @throws CustomException If a user with the provided email or phone already exists,
     *                         a conflict error with HTTP status {@code 409 CONFLICT} is thrown.
     */
    @Override
    public void isExist(String emailOrPhone) {
        Optional<Person> person = authRepository.getByEmailOrPhone(emailOrPhone);
        if (person.isPresent()) {
            throw new UserAlreadyExistsException(emailOrPhone);
        }
    }

    /**
     * Fetches the user's data based on the provided email or phone number.
     * Returns a {@link PersonDTO} object with the user's details, excluding the password.
     *
     * @param emailOrPhone The email or phone number of the user to retrieve.
     * @return {@link PersonDTO} The {@link PersonDTO} containing the user's data,
     * with the password field set to {@code null}.
     * @throws CustomException None (returns {@code null} if user not found, no exception thrown).
     */
    @Override
    public PersonDTO getByEmailOrPhone(String emailOrPhone) {
        Optional<Person> person = authRepository.getByEmailOrPhone(emailOrPhone);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        PersonDTO response = converter.personToDTO(person.get());
        response.setPassword(null);
        return response;
    }

    /**
     * Generates an authentication token for the user based on provided login data
     * (email or phone and password). If credentials are valid, the token is generated and returned.
     *
     * @param loginData The {@link AuthLogin} object containing the email/phone and password of the user
     *                  attempting to log in.
     * @return {@link String} A JWT token if authentication is successful.
     * @throws CustomException If authentication fails, a {@code 403 FORBIDDEN} exception with the
     *                         message "Invalid credentials" is thrown.
     */
    @Override
    public String generateToken(AuthLogin loginData) {
        String token = null;
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.emailOrPhone(), loginData.password()));
        if (authentication.isAuthenticated()) {
            Optional<Person> personToInclude = authRepository.getByEmailOrPhone(loginData.emailOrPhone());
            if (personToInclude.isEmpty()) {
                throw new UsernameNotFoundException("user not found");
            }
            token = jwtService.generateToken(personToInclude.get(), loginData.emailOrPhone());
            if (token == null) {
                throw new NullTokenGeneratedException(personToInclude.get().getId());
            }
        }
        return token;
    }

    /**
     * Checks if the user's email or phone is verified.
     * If not verified, throws an exception.
     *
     * @param loginData The {@link AuthLogin} object containing the email/phone and password.
     * @throws CustomException If the user's email or phone is not verified,
     *                         a {@code 403 FORBIDDEN} exception with the message "Email verification needed" is thrown.
     */
    @Override
    public void isValidated(AuthLogin loginData) {
        if (!authRepository.isVerified(loginData.emailOrPhone())) {
            throw new UnverifiedEmailException(loginData.emailOrPhone());
        }
    }

    /**
     * Checks if the provided email or phone already exists in the system.
     * If either is found, throws a conflict exception.
     *
     * @param email The email to check.
     * @param phone The phone number to check.
     * @throws CustomException If a user with the provided email or phone already exists,
     *                         a conflict exception with HTTP status {@code 409 CONFLICT} is thrown.
     */
    @Override
    public void checkEmailAndPhone(String email, String phone) {
        if (authRepository.getByEmailOrPhone(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
        if (authRepository.getByEmailOrPhone(phone).isPresent()) {
            throw new UserAlreadyExistsException(phone);
        }
    }
}
