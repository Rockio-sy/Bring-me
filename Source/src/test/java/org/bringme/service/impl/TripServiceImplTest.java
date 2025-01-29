package org.bringme.service.impl;

import org.bringme.dto.TripDTO;
import org.bringme.model.Trip;
import org.bringme.repository.TripRepository;
import org.bringme.service.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(TestWatchersExtension.class)
@DisplayName("Trip Service Test")
class TripServiceImplTest {
    @Mock
    private TripRepository tripRepository;
    @Mock
    private Converter converter;
    @InjectMocks
    private TripServiceImpl tripService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Given a TripDTO, when saveTrip, then return TripDTO.")
    void givenTrip_whenSaveTrip_thenReturnTripDTO() {
        // Arrange
        TripDTO dto = new TripDTO(8L, 1, 2, "Airport", 2,
                LocalDateTime.of(2024, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 99L);
        Trip model = new Trip(8L, 1, 2, "Airport", 2,
                LocalDateTime.of(2024, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 99L);

        // Act
        when(converter.DTOtoTrip(dto)).thenReturn(model);
        when(tripRepository.saveTrip(model)).thenReturn(1L);
        when(converter.tripToDTO(model)).thenReturn(dto);
        when(tripRepository.isExist(model)).thenReturn(null);

        TripDTO resp = tripService.saveTrip(dto);

        assertEquals(resp.getId(), 1L);
    }

    @Test
    @DisplayName("Given a TripDTO, when saveTrip, then return 'Trip already exists' exception.")
    void givenTrip_whenSaveTrip_thenReturnCannotCreateTripException() {
        // Arrange
        TripDTO dto = new TripDTO(2L, 1, 2, "Airport", 2,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        Trip model = new Trip(2L, 1, 2, "Airport", 2,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        // Act
        when(converter.DTOtoTrip(dto)).thenReturn(model);
        when(tripRepository.saveTrip(model)).thenReturn(null);
        CustomException ex = assertThrows(CustomException.class, () -> tripService.saveTrip(dto));

        // Assert
        assertEquals("Trip already exist", ex.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
    }

    @Test
    @DisplayName("Given a TripDTO, when validateTrip, then no return")
    void givenTrip_whenValidateTrip_thenNoReturn() {
        TripDTO dto = new TripDTO(2L, 1, 2, "Airport", 2,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        assertDoesNotThrow(() -> tripService.validateTrip(dto));
    }

    @Test
    @DisplayName("Given a TripDTO with same directions, when validateTrip, then return 'Origin and destination cannot be the same' exception.")
    void givenTripWithSameDirections_whenValidateTrip_thenReturnOriginAndDestinationCannotBeTheSameException() {
        TripDTO dto = new TripDTO(2L, 1, 1, "Airport", 2,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        CustomException ex = assertThrows(CustomException.class, () -> tripService.validateTrip(dto));
        assertEquals("Origin and destination cannot be the same.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given a TripDTO with zero value for origin, when validateTrip, then return 'Origin, destination, empty weight, and passenger ID must be non-zero.' exception")
    void givenTripWithZeroValueForOrigin_whenValidateTrip_thenReturnOriginDestinationEmptyWeightAndPassengerIdMustBeNonZeroException() {
        TripDTO dto = new TripDTO(2L, 0, 1, "Airport", 2,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        CustomException ex = assertThrows(CustomException.class, () -> tripService.validateTrip(dto));
        assertEquals("Origin, destination, empty weight, and passenger ID must be non-zero.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given a TripDTO with zero value for destination, when validateTrip, then return 'Origin, destination, empty weight, and passenger ID must be non-zero.' exception")
    void givenTripWithZeroValueForDestination_whenValidateTrip_thenReturnOriginDestinationEmptyWeightAndPassengerIdMustBeNonZeroException() {
        TripDTO dto = new TripDTO(2L, 1, 0, "Airport", 2,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        CustomException ex = assertThrows(CustomException.class, () -> tripService.validateTrip(dto));
        assertEquals("Origin, destination, empty weight, and passenger ID must be non-zero.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given a TripDTO with zero value for empty weight, when validateTrip, then return 'Origin, destination, empty weight, and passenger ID must be non-zero.' exception")
    void givenTripWithZeroValueForEmptyWeight_whenValidateTrip_thenReturnOriginDestinationEmptyWeightAndPassengerIdMustBeNonZeroException() {
        TripDTO dto = new TripDTO(2L, 1, 2, "Airport", 0,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        CustomException ex = assertThrows(CustomException.class, () -> tripService.validateTrip(dto));
        assertEquals("Origin, destination, empty weight, and passenger ID must be non-zero.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given a TripDTO with zero value for passenger id, when validateTrip, then return 'Origin, destination, empty weight, and passenger ID must be non-zero.' exception")
    void givenTripWithZeroValueForPassengerId_whenValidateTrip_thenReturnOriginDestinationEmptyWeightAndPassengerIdMustBeNonZeroException() {
        TripDTO dto = new TripDTO(2L, 1, 2, "Airport", 3,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 0L);

        CustomException ex = assertThrows(CustomException.class, () -> tripService.validateTrip(dto));
        assertEquals("Origin, destination, empty weight, and passenger ID must be non-zero.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given a TripDTO with past departure time, when validateTrip, then return 'Departure time cannot be in the past.' exception")
    void givenTripWithPastDepartureTime_whenValidateTrip_thenReturnDepartureTimeCannotBeInThePastException() {
        TripDTO dto = new TripDTO(2L, 1, 2, "Airport", 3,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2024, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        CustomException ex = assertThrows(CustomException.class, () -> tripService.validateTrip(dto));
        assertEquals("Departure time cannot be in the past.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given a TripDTO with incompatible times, when validateTrip, then return 'Departure time cannot be in the past.' exception")
    void givenTripWithIncompatibleTimes_whenValidateTrip_thenReturnDepartureTimeCannotBeInThePastException() {
        TripDTO dto = new TripDTO(2L, 1, 2, "Airport", 3,
                LocalDateTime.of(2026, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2027, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        CustomException ex = assertThrows(CustomException.class, () -> tripService.validateTrip(dto));
        assertEquals("Arrival time cannot be before departure time.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Given a TripDTO with incompatible minutes, when validateTrip, then return 'Departure time cannot be in the past.' exception")
    void givenTripWithIncompatibleMinutes_whenValidateTrip_thenReturnDepartureTimeCannotBeInThePastException() {
        TripDTO dto = new TripDTO(2L, 1, 2, "Airport", 3,
                LocalDateTime.of(2029, Month.DECEMBER, 3, 16, 0),
                LocalDateTime.of(2029, Month.DECEMBER, 3, 13, 0),
                false, "No comment", 1L);

        CustomException ex = assertThrows(CustomException.class, () -> tripService.validateTrip(dto));
        assertEquals("Flights on the same day must be at least 4 hours long.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

}