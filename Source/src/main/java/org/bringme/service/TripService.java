package org.bringme.service;


import org.bringme.dto.TripDTO;
import org.bringme.model.Trip;

import java.time.LocalDateTime;
import java.util.List;

public interface TripService {
    TripDTO saveTrip(TripDTO tripDTO);

    TripDTO getById(Long id);

    Long isExist(Trip trip);
    List<TripDTO> getAllTrips();

    void validateTrip(TripDTO requestTrip);


    List<TripDTO> filterByCountries(int origin, int destination);
}
