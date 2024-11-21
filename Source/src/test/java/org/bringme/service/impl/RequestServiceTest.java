package org.bringme.service.impl;

import org.bringme.dto.RequestDTO;
import org.bringme.model.Item;
import org.bringme.model.Request;
import org.bringme.model.Trip;
import org.bringme.repository.ItemRepository;
import org.bringme.repository.RequestRepository;
import org.bringme.repository.TripRepository;
import org.bringme.service.EmailService;
import org.bringme.service.NotificationService;
import org.bringme.service.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(TestWatchersExtension.class)
public class RequestServiceTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private Converter converter;
    @Mock
    private EmailService emailService;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private RequestServiceImpl requestService;

    @BeforeEach
    void setUp(){
        openMocks(this);
    }

    @Test
    @DisplayName("Given valid RequestDTO, when saveRequest, then return RequestDTO")
    void givenValidRequestDTO_whenSaveRequest_thenReturnRequestDTO(){
        // Arrange
        RequestDTO dto = new RequestDTO(2L, 1L, 1L, 1, 1, 1, 2, "No comment", false, 0.1F);
        Request model = new Request(1L, 1, 1, 1, 1, 1, 2, "No comment", false, 0.1F);
        Item item = new Item(1L, "test", 1, 2, 2, 2, 2, "Comment", "Address", "photoURL", 1L);
        Trip trip = new Trip(1L, 1, 2, "Airport", 2,
                LocalDateTime.of(2024, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        // Act
        when(itemRepository.getById(any(Long.class))).thenReturn(Optional.of(item));
        when(tripRepository.getById(any(Long.class))).thenReturn(Optional.of(trip));
        when(converter.DTOtoRequest(dto)).thenReturn(model);
        when(requestRepository.saveRequest(model)).thenReturn(1L);
        doNothing().when(emailService).sendEmail(any(String.class), any(String.class),any(Long.class));
        doNothing().when(notificationService).saveNotification(any(Integer.class), any(String.class), any(Integer.class));
        when(converter.requestToDTO(model)).thenReturn(dto);

        RequestDTO response = requestService.saveRequest(dto);

        assertEquals(1L, response.getId());

        verify(emailService, times(2)).sendEmail(any(String.class), any(String.class),any(Long.class));
        verify(notificationService, times(2)).saveNotification(any(Integer.class), any(String.class), any(Integer.class));
    }

    @Test
    @DisplayName("Given requestDTO, when saveRequest, then return 'Item or trip are not found' exception.")
    void givenRequestDTO_whenSaveRequest_thenReturnItemOrTripNotFoundException(){
        RequestDTO dto = new RequestDTO(2L, 1L, 1L, 1, 1, 1, 2, "No comment", false, 0.1F);

        when(itemRepository.getById(any(Long.class))).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> requestService.saveRequest(dto));
        assertEquals("Item or trip are not found", ex.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
    }

    @Test
    @DisplayName("Given RequestDTO with incompatible directions(Origin), when saveRequest, then return 'Directions are incompatible' exception.")
    void givenRequestDTO_whenSaveRequest_thenReturnDirectionsAreIncompatibleException(){
        // Arrange
        RequestDTO dto = new RequestDTO(2L, 1L, 1L, 1, 1, 1, 2, "No comment", false, 0.1F);
        Item item = new Item(1L, "test", 1, 3, 2, 2, 2, "Comment", "Address", "photoURL", 1L);
        Trip trip = new Trip(1L, 2, 3, "Airport", 2,
                LocalDateTime.of(2024, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        // Act
        when(itemRepository.getById(any(Long.class))).thenReturn(Optional.of(item));
        when(tripRepository.getById(any(Long.class))).thenReturn(Optional.of(trip));

        CustomException ex = assertThrows(CustomException.class, () -> requestService.saveRequest(dto));
        assertEquals("Directions are incompatible", ex.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());

    }

    @Test
    @DisplayName("Given RequestDTO with incompatible directions(Destination), when saveRequest, then return 'Directions are incompatible' exception.")
    void givenRequestDTOWithIncompatibleOrigins_whenSaveRequest_thenReturnDirectionsAreIncompatibleException(){
        // Arrange
        RequestDTO dto = new RequestDTO(2L, 1L, 1L, 1, 1, 1, 2, "No comment", false, 0.1F);
        Item item = new Item(1L, "test", 1, 4, 2, 2, 2, "Comment", "Address", "photoURL", 1L);
        Trip trip = new Trip(1L, 1, 3, "Airport", 2,
                LocalDateTime.of(2024, Month.APRIL, 3, 13, 43),
                LocalDateTime.of(2025, Month.APRIL, 3, 13, 43),
                false, "No comment", 1L);

        // Act
        when(itemRepository.getById(any(Long.class))).thenReturn(Optional.of(item));
        when(tripRepository.getById(any(Long.class))).thenReturn(Optional.of(trip));

        CustomException ex = assertThrows(CustomException.class, () -> requestService.saveRequest(dto));
        assertEquals("Directions are incompatible", ex.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());

    }
}
