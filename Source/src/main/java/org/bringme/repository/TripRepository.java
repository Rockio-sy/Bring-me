package org.bringme.repository;

import org.bringme.model.Trip;

import java.util.Optional;

public interface TripRepository {
    Long saveTrip(Trip trip);
    Optional<Trip> getById(Long id);
}
