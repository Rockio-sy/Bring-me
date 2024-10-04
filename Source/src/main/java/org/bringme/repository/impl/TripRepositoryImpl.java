package org.bringme.repository.impl;

import org.bringme.model.Trip;
import org.bringme.repository.TripRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TripRepositoryImpl implements TripRepository {

    private final JdbcTemplate jdbcTemplate;

    public TripRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveTrip(Trip trip){
        String sql = "INSERT INTO trips(origin, destination, destination_airport, empty_weight," +
                " departure_time, arrival_time, transit, passenger_id, comments)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                ps.setString(9, trip.getComments());
                return ps;
            }, keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Trip> getById(Long id){
        String sql = "SELECT * FROM trips WHERE id = ?";
        return jdbcTemplate.query(sql, new TripRowMapper(), id)
                .stream()
                .findFirst();
    }

    // RowMapper
    public static final class TripRowMapper implements RowMapper<Trip>{
        @Override
        public Trip mapRow(ResultSet rs, int rowNum) throws SQLException{
            Trip newTrip = new Trip();
            newTrip.setId(rs.getLong("id"));
            newTrip.setOrigin(rs.getInt("origin"));
            newTrip.setDestination(rs.getInt("destination"));
            newTrip.setDestinationAirport(rs.getString("destination_airport"));
            newTrip.setEmptyWeight(rs.getFloat("empty_weight"));
            newTrip.setArrivalTime(rs.getTimestamp("arrival_time").toLocalDateTime());
            newTrip.setDepartureTime(rs.getTimestamp("departure_time").toLocalDateTime());
            newTrip.setTransit(rs.getBoolean("transit"));
            newTrip.setComments(rs.getString("comments"));
            newTrip.setPassengerId(rs.getInt("passenger_id"));
            return newTrip;
        }
    }

}
