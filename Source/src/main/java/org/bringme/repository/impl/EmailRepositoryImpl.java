package org.bringme.repository.impl;

import org.bringme.repository.EmailRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class EmailRepositoryImpl implements EmailRepository {
    private final JdbcTemplate jdbcTemplate;

    public EmailRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveCode(String email, String code) {
        String checkSql = "SELECT id FROM verification WHERE email = ?";
        try {
            jdbcTemplate.queryForObject(checkSql, (rs, rowNum) -> rs.getLong("id"), email);


        } catch (EmptyResultDataAccessException e) {
            String insertSql = "INSERT INTO verification(code, email) VALUES" +
                    "(?, ?)";
            jdbcTemplate.update(insertSql, code, email);
            return;
        }
        String updateSql = "UPDATE verification SET code = ? WHERE email = ?";
        jdbcTemplate.update(updateSql, code, email);
    }

    @Override
    public String getCode(String email) {
        String sql = "SElECT code FROM verification WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString("code"), email);
    }
}
