package org.bringme.utils;

import org.bringme.service.impl.EmailServiceImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Cleans database of every temporary data
 */
@Component
public class DatabaseCleaner {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Cleans database by soft remove the verification codes that created in {@link org.bringme.service.EmailService#sendVerificationCode(String)
     * sendVerifcationCode} after more than 30 min
     * Function implemented every day at 00:00
     * @throws SQLException If database couldn't run it
     */
    public void cleanExpiredVerificationCode() throws SQLException{
        String sql = "UPDATE verification SET removed_at = CURRENT_TIMESTAMP WHERE (CURRENT_TIMESTAMP - created_at) > INTERVAL '30 minutes'";
        jdbcTemplate.update(sql);
        System.out.println("Cleaned");
    }
}
