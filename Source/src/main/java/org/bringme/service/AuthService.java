package org.bringme.service;

import org.bringme.dto.PersonDTO;
import org.bringme.model.AuthLogin;

public interface AuthService {
    PersonDTO signUp(PersonDTO requestPerson);

    boolean isExist(String emailOrPhone);

    PersonDTO getByEmailOrPhone(String emailOrPhone);

    String verify(AuthLogin loginData);
}
