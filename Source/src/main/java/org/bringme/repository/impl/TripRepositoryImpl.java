package org.bringme.repository.impl;

import org.bringme.model.Trip;
import org.bringme.repository.TripRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TripRepositoryImpl implements TripRepository {

    private final JdbcTemplate jdbcTemplate;

    public TripRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveTrip(Trip trip) {
        String sql = "INSERT INTO trips(origin, destination, destination_airport, empty_weight," +
                " departure_time, arrival_time, transit, passenger_id, comments)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, trip.getOrigin());
                ps.setInt(2, trip.getDestination());
                ps.setString(3, trip.getDestinationAirport());
                ps.setFloat(4, trip.getEmptyWeight());
                ps.setTimestamp(5, Timestamp.valueOf(trip.getDepartureTime()));
                ps.setTimestamp(6, Timestamp.valueOf(trip.getArrivalTime()));
                ps.setBoolean(7, trip.isTransit());
                ps.setInt(8, trip.getPassengerId().intValue());
                ps.setString(9, trip.getComments());
                return ps;
            }, keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Trip> getById(Long id) {
        String sql = "SELECT * FROM trips WHERE id = ?";
        return jdbcTemplate.query(sql, new TripRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Trip> getAll() {
        String sql = "SELECT * FROM trips";
        List<Trip> savedList = jdbcTemplate.query(sql, new TripRowMapper());
        if (savedList.isEmpty()) {
            return null;
        }
        return savedList;
    }
/*SELECT id FROM trips WHERE removed_at IS NULL AND origin = 966 AND destination = 926 AND destination_airport = 'SVO' AND empty_weight = 3 AND departure_time = '2026-12-26 05:56:38.867 ' AND arrival_time = '2027-11-22 05:56:38.867' AND transit =
 TRUE AND passenger_id = 1;
*/
    @Override
    public Long isExist(Trip trip) {
        String sql = "SELECT id FROM trips WHERE removed_at IS NULL AND origin = ? AND destination = ? AND " +
                "destination_airport = ? AND empty_weight = ? AND departure_time = ? AND arrival_time = ? AND transit = ? AND " +
                "passenger_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"),
                    trip.getOrigin(), trip.getDestination(), trip.getDestinationAirport(),
                    trip.getEmptyWeight(), trip.getDepartureTime(), trip.getArrivalTime(),
                    trip.isTransit(), trip.getPassengerId());
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    @Override
    public List<Trip> filterByCountries(int origin, int destination) {
        String sql = "SELECT * FROM trips WHERE origin = ? AND destination = ?";
        return jdbcTemplate.query(sql, new TripRowMapper(), origin, destination);
    }

    // RowMapper
    public static final class TripRowMapper implements RowMapper<Trip> {
        @Override
        public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
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
            newTrip.setPassengerId(Integer.toUnsignedLong(rs.getInt("passenger_id")));
            return newTrip;
        }
    }

}
