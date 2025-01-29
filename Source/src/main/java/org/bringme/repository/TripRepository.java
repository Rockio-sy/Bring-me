package org.bringme.repository;

import org.bringme.model.Trip;

import java.util.List;
import java.util.Optional;

public interface TripRepository {
    Long saveTrip(Trip trip);
    Optional<Trip> getById(Long id);
    List<Trip> getAll();

    Long isExist(Trip trip);
    List<Trip> filterByCountries(int origin, int destination);
}
