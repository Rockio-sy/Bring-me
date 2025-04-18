package org.bringme.service.impl;


import org.bringme.dto.PersonDTO;
import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
import org.bringme.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(TestWatchersExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private Converter converter;

    @InjectMocks
    PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given valid old and new password, when updatePassword, then no return.")
    void givenPasswords_whenUpdatePassword_thenNoReturn() {
        // Arrange
        Long userId = 1L;
        Person mockPerson = new Person(userId, "test", "test", "address", "email@email.com", "1231231231", "oldHashedPassword", 1, "USER");

        when(personRepository.getById(userId)).thenReturn(Optional.of(mockPerson));
        when(passwordEncoder.matches("oldPassword", mockPerson.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(any(String.class))).thenReturn("newHashedPassword");
        when(personRepository.updatePassword(userId, "newHashedPassword")).thenReturn(1);

        assertDoesNotThrow(() -> personService.updatePassword(userId, "newPassword", "oldPassword"));
        verify(personRepository).updatePassword(userId, "newHashedPassword");
    }

    @Test
    @DisplayName("Given incorrect old password, when updatePassword, then return exception of bad request.")
    void givenIncorrectOldPassword_whenUpdatePassword_ReturnExceptionBadRequest() {
        Long userId = 1L;
        Person mockPerson = new Person(userId, "test", "test", "address", "email@email.com", "1231231231", "oldHashedPassword", 1, "USER");
        when(personRepository.getById(userId)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> personService.updatePassword(userId, "asd", "asd"));
        assertEquals("User not found", ex.getMessage());
        assertEquals(HttpStatus.NO_CONTENT, ex.getStatus());


    }

    @Test
    @DisplayName("Given valid user, when createNewUser, then return userDTO")
    void givenValidUser_whenCreateNewUser_thenReturnUserDTO() {
        PersonDTO mockPerson = new PersonDTO("test", "test", "address", "email@email.com", "1231231231", "123345678s", "ADMIN" );
        when(converter.DTOtoPerson(mockPerson)).thenReturn(new Person());
        when(personRepository.save(any(Person.class))).thenReturn(1L);
        PersonDTO dto = personService.createNewUser(mockPerson);
        assertNotNull(dto);
        assertEquals("ADMIN", dto.getRole());
    }
}