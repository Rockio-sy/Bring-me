package org.bringme.service;

import org.bringme.dto.PersonDTO;

public interface PersonService {
    PersonDTO getPersonById(Long id);
    PersonDTO getByEmail(String email);
    PersonDTO getByPhone(String phone);
    void updatePassword(Long userId, String newPassword, String oldPassword);

    PersonDTO showPersonDetails(int hostId);

    PersonDTO createNewUser(PersonDTO newUser);

    void bandUser(Long id);

    void unBandUser(Long id);
}
