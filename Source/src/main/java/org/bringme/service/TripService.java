package org.bringme.service;


import org.bringme.dto.TripDTO;

import java.util.List;

public interface TripService {
    TripDTO saveTrip(TripDTO tripDTO);
    TripDTO getById(Long id);
    List<TripDTO> getAllTrips();
}
