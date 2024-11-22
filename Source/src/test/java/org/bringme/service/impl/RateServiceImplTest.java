package org.bringme.service.impl;

import org.bringme.dto.RateDTO;
import org.bringme.model.Rate;
import org.bringme.repository.PersonRepository;
import org.bringme.repository.RateRepository;
import org.bringme.repository.RequestRepository;
import org.bringme.service.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(TestWatchersExtension.class)
class RateServiceImplTest {

    @Mock
    private RateRepository rateRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Converter converter;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RateServiceImpl rateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given user id with rated user id, when check rating availability, then no return")
    void givenUserIdWithRatedUserId_whenCheckRatingAvailability_thenSuccess() {
        // Act
        when(requestRepository.isThereCommonRequest(any(Long.class), any(Integer.class))).thenReturn(true);
        assertDoesNotThrow(() -> rateService.checkRatingAvailability(1L, 1));
    }

    @Test
    @DisplayName("Given user id with rated user id, when check rating availability, then return 'No common requests' exception.")
    void givenUserIdWithRatedUserId_whenCheckRatingAvailability_thenNoCommonRequestsException() {
        // Act
        when(requestRepository.isThereCommonRequest(any(Long.class), any(Integer.class))).thenReturn(false);
        CustomException ex = Assertions.assertThrows(CustomException.class, () -> rateService.checkRatingAvailability(1L, 1));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("No common requests", ex.getMessage());
    }

    @Test
    @DisplayName("Given a rate, when create new rate, then return success.")
    void givenRate_whenCreateNewRate_thenReturnSuccess() {
        // Arrange
        RateDTO dto = new RateDTO(1L, 1, "No comment", 3, 1);
        Rate model = new Rate(1L, "No comment", 3, 1, 1);
        // Act
        when(converter.DTOtoRate(dto)).thenReturn(model);
        when(rateRepository.save(model)).thenReturn(1L);
        when(converter.rateToDTO(model)).thenReturn(dto);

        rateService.createNewRate(dto);

        verify(converter, times(1)).DTOtoRate(dto);
        verify(rateRepository, times(1)).save(model);
        verify(converter, times(1)).rateToDTO(model);
    }

    @Test
    @DisplayName("Given a rate, when createNewRate, then return 'Cannot create the rate' exception.")
    void givenRate_whenCreateNewRate_thenReturnCannotCreateRateException() {
        // Arrange
        RateDTO dto = new RateDTO(1L, 1, "No comment", 3, 1);
        Rate model = new Rate(1L, "No comment", 3, 1, 1);
        // Act
        when(converter.DTOtoRate(dto)).thenReturn(model);
        when(rateRepository.save(model)).thenReturn(null);

        CustomException ex = assertThrows(CustomException.class, () -> rateService.createNewRate(dto));
        assertEquals("Cannot create the rate", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());

    }

}
