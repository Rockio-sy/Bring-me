package org.bringme.repository;

import org.bringme.model.Trip;

public interface TripRepository {
    Long saveTrip(Trip trip);
}
