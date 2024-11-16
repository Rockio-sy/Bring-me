package org.bringme.repository.impl;

import org.bringme.model.Item;
import org.bringme.model.Rate;
import org.bringme.repository.RateRepository;
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

@Repository
public class RateRepositoryImpl implements RateRepository {
    private final JdbcTemplate jdbcTemplate;

    public RateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Rate> getAll(int userId) {
        String sql = "SELECT * FROM rates WHERE rated_user_id = ?";
        return jdbcTemplate.query(sql, new RateRowMapper(), userId);
    }

    @Override
    public Long save(Rate model) {
        String sql = "INSERT INTO rates (rated_user_id, request_id, rate_value, comments) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, model.getUserId());
                ps.setInt(2, model.getRequestId());
                ps.setInt(3, model.getValue());
                ps.setString(4, model.getComments());
                return ps;
            }, keyHolder);

            // Return the generated key (ID of the inserted row)
            return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }



/*
* CREATE TABLE rates(
id SERIAL PRIMARY KEY,
rated_user_id INT NOT NULL,
request_id INT NOT NULL,
rate_value INT
comments TEXT DEFAULT '',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
CONSTRAINT fk_user_id FOREIGN KEY (rated_user_id) REFERENCES persons(id) ON DELETE CASCADE,
CONSTRAINT fk_request_id FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE
);*/

    public static final class RateRowMapper implements RowMapper<Rate> {
        @Override
        public Rate mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rate newRate = new Rate();
            newRate.setId(rs.getLong("id"));
            newRate.setRequestId(rs.getInt("request_id"));
            newRate.setComments(rs.getString("comments"));
            newRate.setUserId(rs.getInt("rated_user_id"));
            newRate.setValue(rs.getInt("rate_value"));
            return newRate;
        }
    }
}