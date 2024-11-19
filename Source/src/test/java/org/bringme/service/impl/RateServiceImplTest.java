package org.bringme.service.impl;

import org.bringme.dto.RateDTO;
import org.bringme.model.Person;
import org.bringme.model.Rate;
import org.bringme.repository.PersonRepository;
import org.bringme.repository.RateRepository;
import org.bringme.utils.Converter;
import org.bringme.service.exceptions.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateServiceImplTest {

    @Mock
    private RateRepository rateRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Converter converter;

    @InjectMocks
    private RateServiceImpl rateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRates_UserNotFound() {
        // Mock personRepository to return empty
        when(personRepository.getById(anyLong())).thenReturn(Optional.empty());

        // Assert exception is thrown
        CustomException exception = assertThrows(CustomException.class, () -> rateService.getAllRates(1));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }


    @Test
    void testGetAllRates_NoDataFound() {
        // Mock personRepository to return non-empty
        when(personRepository.getById(anyLong())).thenReturn(Optional.of(new Person()));  // Mocked person

        // Mock rateRepository to return empty list
        when(rateRepository.getAll(anyInt())).thenReturn(Collections.emptyList());

        // Assert exception is thrown
        CustomException exception = assertThrows(CustomException.class, () -> rateService.getAllRates(1));
        assertEquals("No data", exception.getMessage());
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatus());
    }

    @Test
    void testGetAllRates_Success() {
        // Mock personRepository to return non-empty
        when(personRepository.getById(anyLong())).thenReturn(Optional.of(new Person())); // Mocked person

        // Mock rateRepository to return sample data
        Rate rate = new Rate(); // Assuming Rate has default constructor
        when(rateRepository.getAll(anyInt())).thenReturn(List.of(rate));

        // Mock converter to convert Rate to RateDTO
        RateDTO rateDTO = new RateDTO(1L, 2, "Good service", 5, 123); // Using the constructor of RateDTO
        when(converter.rateToDTO(rate)).thenReturn(rateDTO);

        // Execute method
        List<RateDTO> result = rateService.getAllRates(1);

        // Verify behavior
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rateDTO, result.get(0)); // Ensure the returned RateDTO matches
    }
}
