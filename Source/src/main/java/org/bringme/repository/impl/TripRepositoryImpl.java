package org.bringme.repository.impl;

import org.bringme.model.Trip;
import org.bringme.repository.TripRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Objects;

@Repository
public class TripRepositoryImpl implements TripRepository {

    private final JdbcTemplate jdbcTemplate;

    public TripRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveTrip(Trip trip){
        String sql = "INSERT INTO trips(origin, destination, destination_airport, empty_weight," +
                " departure_time, arrival_time, transit, passenger_id)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            jdbcTemplate.update(connection ->{
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, trip.getOrigin());
                ps.setInt(2, trip.getDestination());
                ps.setString(3, trip.getDestinationAirport());
                ps.setFloat(4, trip.getEmptyWeight());
                ps.setTimestamp(5, Timestamp.valueOf(trip.getDepartureTime()));
                ps.setTimestamp(6, Timestamp.valueOf(trip.getArrivalTime()));
                ps.setBoolean(7, trip.isTransit());
                ps.setInt(8, trip.getPassengerId());
                return ps;
            }, keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
