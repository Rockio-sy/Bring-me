package org.bringme.repository.impl;

import org.bringme.model.Request;
import org.bringme.repository.RequestRepository;
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

    //Fix the repository and handel Exceptions
    private final JdbcTemplate jdbcTemplate;

    public RequestRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Request> getAll(Long userId) {
        String sql = "SELECT * FROM requests WHERE requester_user_id = ? OR requested_user_id = ?";
        return jdbcTemplate.query(sql, new requestRowMapper(), userId, userId);
    }

    @Override
    public Long saveRequest(Request request) {
        String sql = "INSERT INTO requests (requested_user_id, requester_user_id," +
                " item_id, trip_id, origin, destination, comments, price)" +
                " VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            jdbcTemplate.update(connection ->{
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
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public Optional<Request> getRequestById(Long id) {
        String sql = "SELECT * FROM requests WHERE id = ?";

        return jdbcTemplate.query(sql, new requestRowMapper(), id)
                .stream()
                .findFirst();
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
