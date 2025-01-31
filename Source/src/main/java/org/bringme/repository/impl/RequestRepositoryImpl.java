package org.bringme.repository.impl;

import org.bringme.model.Request;
import org.bringme.repository.RequestRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RequestRepositoryImpl implements RequestRepository {

    private final JdbcTemplate jdbcTemplate;

    public RequestRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * Retrieves all requests related to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link Request} objects where the user is either the requester or the requested user
     */
    @Override
    public List<Request> getAll(Long userId) {
        String sql = "SELECT * FROM requests WHERE requester_user_id = ? OR requested_user_id = ?";
        return jdbcTemplate.query(sql, new requestRowMapper(), userId, userId);
    }

    /**
     * Saves a new request in the database.
     *
     * @param request the {@link Request} object to be saved
     * @return the generated ID of the inserted request or {@code null} if an error occurs
     */
    @Override
    public Long saveRequest(Request request) {
        String sql = "INSERT INTO requests (requested_user_id, requester_user_id," +
                " item_id, trip_id, origin, destination, comments, price)" +
                " VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, request.getRequestedUserId());
                ps.setInt(2, request.getRequesterUserId());
                ps.setInt(3, request.getItemId());
                ps.setInt(4, request.getTripId());
                ps.setInt(5, request.getOrigin());
                ps.setInt(6, request.getDestination());
                ps.setString(7, request.getComments());
                ps.setFloat(8, request.getPrice());
                return ps;
            }, keyHolder);

            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    /**
     * Retrieves a specific request by its ID.
     *
     * @param id the ID of the request
     * @return an {@link Optional} containing the {@link Request} if found, otherwise empty
     */
    @Override
    public Optional<Request> getRequestById(Long id) {
        String sql = "SELECT * FROM requests WHERE id = ?";

        return jdbcTemplate.query(sql, new requestRowMapper(), id)
                .stream()
                .findFirst();
    }

    /**
     * Checks if a request exists for a given item and trip.
     *
     * @param itemId the ID of the item
     * @param tripId the ID of the trip
     * @return the ID of the request if it exists, otherwise {@code null}
     */
    @Override
    public Long isExists(Integer itemId, Integer tripId) {
        String sql = "SELECT id FROM requests WHERE item_id = ? AND trip_id = ? AND removed_at IS NULL";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"), itemId, tripId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves all requests sent by a user.
     *
     * @param userId the ID of the user
     * @return a list of {@link Request} objects sent by the user
     */
    @Override
    public List<Request> getSentRequests(Long userId) {
        String sql = "SELECT * FROM requests WHERE requested_user_id = ?";
        return jdbcTemplate.query(sql, new requestRowMapper(), userId.intValue());
    }

    /**
     * Retrieves requests based on the user's ID, origin, and destination.
     *
     * @param userId      the ID of the user
     * @param origin      the origin location
     * @param destination the destination location
     * @return a list of matching {@link Request} objects
     */
    @Override
    public List<Request> getByDirections(Long userId, int origin, int destination) {
        String sql = "SELECT * FROM requests WHERE (requester_user_id = ? OR requested_user_id = ?) AND (origin = ? AND destination = ?)";
        return jdbcTemplate.query(sql, new requestRowMapper(), userId.intValue(), userId.intValue(), origin, destination);
    }

    /**
     * Retrieves all requests received by a user.
     *
     * @param userId the ID of the user
     * @return a list of {@link Request} objects where the user is the requested user
     */
    @Override
    public List<Request> getReceivedRequests(Long userId) {
        String sql = "SELECT * FROM requests WHERE requester_user_id = ? OR requested_user_id = ?";
        return jdbcTemplate.query(sql, new requestRowMapper(), userId.intValue(), userId.intValue());
    }

    /**
     * Approves a request by updating its approval status.
     *
     * @param requestId the ID of the request to approve
     */
    @Override
    public void approveRequest(Long requestId) {
        String sql = "UPDATE requests SET approvement_statue = TRUE WHERE id = ?";
        jdbcTemplate.update(sql, requestId.intValue());
    }

    /**
     * Filters requests by approval status (approved requests).
     *
     * @param userId the ID of the user
     * @return a list of approved {@link Request} objects for the user
     */
    @Override
    public List<Request> filterByApprovement(Long userId) {
        String sql = "SELECT * FROM requests WHERE requester_user_id = ? AND  approvement_statue = TRUE";
        return jdbcTemplate.query(sql, new requestRowMapper(), userId.intValue());
    }

    /**
     * Filters requests that are still waiting for approval.
     *
     * @param userId the ID of the user
     * @return a list of pending {@link Request} objects for the user
     */
    @Override
    public List<Request> filterByWait(Long userId) {
        String sql = "SELECT * FROM requests WHERE requester_user_id = ? AND approvement_statue = FALSE";
        return jdbcTemplate.query(sql, new requestRowMapper(), userId.intValue());
    }

    /**
     * Checks if there is a common approved request between two users.
     *
     * @param guestId the ID of the guest user
     * @param hostId  the ID of the host user
     * @return {@code true} if there is a common approved request, otherwise {@code false}
     */
    @Override
    public boolean isThereCommonRequest(Long guestId, int hostId) {
        String sql = "SELECT TRUE " +
                "WHERE EXISTS (" +
                "SELECT 1 FROM requests " +
                "WHERE ((requester_user_id = ? AND requested_user_id = ?) " +
                "OR (requester_user_id = ? AND requested_user_id = ?)) " +
                "AND removed_at IS NULL " +
                "AND approvement_statue IS TRUE)";

        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, (rs, numRow) -> (rs.getBoolean(1)), guestId.intValue(), hostId, hostId, guestId.intValue()));
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public static final class requestRowMapper implements RowMapper<Request> {
        @Override
        public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
            Request newRequest = new Request();
            newRequest.setId(rs.getLong("id"));
            newRequest.setRequestedUserId(rs.getInt("requested_user_id"));
            newRequest.setRequesterUserId(rs.getInt("requester_user_id"));
            newRequest.setItemId(rs.getInt("item_id"));
            newRequest.setTripId(rs.getInt("trip_id"));
            newRequest.setOrigin(rs.getInt("origin"));
            newRequest.setDestination(rs.getInt("destination"));
            newRequest.setApprovement(rs.getBoolean("approvement_statue"));
            newRequest.setPrice(rs.getFloat("price"));
            newRequest.setComments(rs.getString("comments"));
            return newRequest;
        }
    }
}
