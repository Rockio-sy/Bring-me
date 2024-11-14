package org.bringme.service;

import org.bringme.dto.PersonDTO;
import org.bringme.dto.AuthLogin;

public interface AuthService {
    PersonDTO signUp(PersonDTO requestPerson);

    void isExist(String emailOrPhone);

    PersonDTO getByEmailOrPhone(String emailOrPhone);

    String generateToken(AuthLogin loginData);

    void isValidated(AuthLogin loginData);
}
