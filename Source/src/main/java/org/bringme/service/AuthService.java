package org.bringme.service;

import org.bringme.dto.PersonDTO;
import org.bringme.dto.AuthLogin;

public interface AuthService {
    PersonDTO signUp(PersonDTO requestPerson);

    boolean isExist(String emailOrPhone);

    PersonDTO getByEmailOrPhone(String emailOrPhone);

    String generateToken(AuthLogin loginData);

    boolean isValidated(AuthLogin loginData);
}
