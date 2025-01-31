package org.bringme.service.impl;

import org.bringme.dto.TripDTO;
import org.bringme.exceptions.CustomException;
import org.bringme.model.Trip;
import org.bringme.repository.TripRepository;
import org.bringme.service.TripService;
import org.bringme.utils.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final Converter converter;

    public TripServiceImpl(TripRepository tripRepository, Converter converter) {
        this.tripRepository = tripRepository;
        this.converter = converter;
    }

    /**
     * Saves a new trip if it does not already exist in the database.
     *
     * @param tripDto The trip details to be saved.
     * @return A TripDTO object containing the saved trip details, including the generated ID.
     * @throws CustomException If the trip already exists, throws FORBIDDEN exception.
     *                         If there is an internal error during saving, throws INTERNAL_SERVER_ERROR exception.
     */
    @Override
    public TripDTO saveTrip(TripDTO tripDto) {
        // Convert to Trip model
        Trip newTrip = converter.DTOtoTrip(tripDto);

        if (isExist(newTrip) != null) {
            throw new CustomException("Trip already exist", HttpStatus.FORBIDDEN);
        }
        Long generatedId = tripRepository.saveTrip(newTrip);
        if (generatedId == null) {
            throw new CustomException("Cannot create trip", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Convert to TripDTO then return
        TripDTO responseTrip = converter.tripToDTO(newTrip);
        responseTrip.setId(generatedId);
        return responseTrip;
    }

    /**
     * Retrieves a trip by its ID from the database.
     *
     * @param id The ID of the trip to retrieve.
     * @return A TripDTO object containing the trip details.
     * @throws CustomException If the trip is not found, throws NO_CONTENT exception.
     */
    @Override
    public TripDTO getById(Long id) {
        // Get trip from database
        Optional<Trip> savedTrip = tripRepository.getById(id);
        if (savedTrip.isEmpty()) {
            throw new CustomException("Trip not found", HttpStatus.NO_CONTENT);
        }
        return converter.tripToDTO(savedTrip.get());
    }

    /**
     * Checks if a trip already exists in the database.
     *
     * @param trip The trip to check for existence.
     * @return The ID of the trip if it exists, null otherwise.
     */
    @Override
    public Long isExist(Trip trip) {
        return tripRepository.isExist(trip);
    }

    /**
     * Retrieves all trips from the database.
     *
     * @return A list of TripDTO objects representing all trips.
     * @throws CustomException If no trips are found, throws NO_CONTENT exception.
     */
    @Override
    public List<TripDTO> getAllTrips() {
        // Get list from database
        List<Trip> savedList = tripRepository.getAll();

        //Check if list is empty
        if (savedList.isEmpty()) {
            throw new CustomException("No content", HttpStatus.NO_CONTENT);
        }

        // Convert every trip to DTO class and save it in new list
        List<TripDTO> responseList = new ArrayList<>();
        for (Trip savedTrip : savedList) {
            TripDTO convertedTrip = converter.tripToDTO(savedTrip);
            responseList.add(convertedTrip);
        }

        return responseList;
    }

    /**
     * Validates the trip data to ensure that it adheres to the necessary constraints.
     *
     * @param requestTrip The trip data to validate.
     * @throws CustomException If any validation fails, throws BAD_REQUEST exception with a relevant message.
     */
    @Override
    public void validateTrip(TripDTO requestTrip) {

        // 1. Check if origin and destination are the same
        if (requestTrip.getOrigin() == requestTrip.getDestination()) {
            throw new CustomException("Origin and destination cannot be the same.", HttpStatus.BAD_REQUEST);
        }

        // 2. Check if origin, destination, empty weight, or passenger ID is zero
        if (requestTrip.getOrigin() == 0 || requestTrip.getDestination() == 0
                || requestTrip.getEmptyWeight() == 0 || requestTrip.getPassengerId() == 0) {
            throw new CustomException("Origin, destination, empty weight, and passenger ID must be non-zero.", HttpStatus.BAD_REQUEST);
        }

        // 4. Check if departure time is in the past
        if (requestTrip.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new CustomException("Departure time cannot be in the past.", HttpStatus.BAD_REQUEST);
        }

        // 5. Check if arrival time is before departure time
        if (requestTrip.getArrivalTime().isBefore(requestTrip.getDepartureTime())) {
            throw new CustomException("Arrival time cannot be before departure time.", HttpStatus.BAD_REQUEST);
        }

        // 6. Optional: Ensure flights on the same day are at least 4 hours apart
        Duration flightDuration = Duration.between(requestTrip.getDepartureTime(), requestTrip.getArrivalTime());
        if (requestTrip.getDepartureTime().toLocalDate().equals(requestTrip.getArrivalTime().toLocalDate())
                && flightDuration.toHours() < 4) {
            throw new CustomException("Flights on the same day must be at least 4 hours long.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Filters trips based on origin and destination country IDs.
     *
     * @param origin      The origin country ID.
     * @param destination The destination country ID.
     * @return A list of TripDTO objects representing trips that match the specified origin and destination.
     * @throws CustomException If no trips are found, throws NO_CONTENT exception.
     */
    @Override
    public List<TripDTO> filterByCountries(int origin, int destination) {
        // Get data from database
        List<Trip> data = tripRepository.filterByCountries(origin, destination);

        // Check
        if (data.isEmpty()) {
            throw new CustomException("No content", HttpStatus.NO_CONTENT);
        }

        // Convert
        List<TripDTO> response = new ArrayList<>();
        for (Trip trip : data) {
            TripDTO dto = converter.tripToDTO(trip);
            response.add(dto);
        }
        return response;
    }
}
