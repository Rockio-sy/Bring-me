package org.bringme.repository.impl;

import org.bringme.model.Request;
import org.bringme.repository.RequestRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RequestRepositoryImpl implements RequestRepository {

    //Fix the repository and handel Exceptions
    private final JdbcTemplate jdbcTemplate;

    public RequestRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Request> getAll() {
        String sql = "SELECT * FROM requests";
        return jdbcTemplate.query(sql, new requestRowMapper());
    }

    @Override
    public int saveRequest(Request request) {
        String sql = "INSERT INTO requests(requester_user_id, requested_user_id, item_id) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                request.getRequesterUserId(),
                request.getRequestedUserId(),
                request.getItemId());
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
        public Request mapRow(ResultSet rs, int rowNum) throws SQLException{
            Request newRequest = new Request();
            newRequest.setId(rs.getLong("id"));
            newRequest.setRequestedUserId(rs.getInt("requested_user_id"));
            newRequest.setRequesterUserId(rs.getInt("requester_user_id"));
            newRequest.setItemId(rs.getInt("item_id"));
            return newRequest;
        }
    }
}
