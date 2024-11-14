package org.bringme.service;


import org.bringme.dto.TripDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TripService {
    TripDTO saveTrip(TripDTO tripDTO);

    TripDTO getById(Long id);

    List<TripDTO> getAllTrips();

    void validateTrip(TripDTO requestTrip);


    List<TripDTO> filterByCountries(int origin, int destination);
}
