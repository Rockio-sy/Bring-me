package org.bringme.service;


import org.bringme.dto.TripDTO;

public interface TripService {
    TripDTO saveTrip(TripDTO tripDTO);
    TripDTO getById(Long id);
}
