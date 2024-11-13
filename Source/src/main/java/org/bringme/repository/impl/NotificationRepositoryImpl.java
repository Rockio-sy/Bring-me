package org.bringme.repository.impl;

import org.bringme.model.Notification;
import org.bringme.model.Person;
import org.bringme.repository.NotificationRepository;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    private final JdbcTemplate jdbcTemplate;

    public NotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Notification> getAll(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ?";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), userId);
    }

    @Override
    public List<Notification> getNotMarked(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND marked = 1";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), userId);
    }

    @Override
    public void save(int userId, String content, int requestId) {
        String sql = "INSERT INTO notifications (user_id, content, request_id)" +
                "VALUES" +
                "(?, ?, ?)";
        jdbcTemplate.update(sql, userId, content, requestId);
    }

    @Override
    public void markOneAsRead(int userId, Long id) {
        String sql = "UPDATE TABLE notifications SET marked = 1 WHERE id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void markAllAsRead(int userId) {
        String sql = "UPDATE TABLE notifications SET marked = 1 WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    private static final class NotificationRowMapper implements RowMapper<Notification>{
        @Override
        public Notification mapRow(ResultSet rs, int rowNum) throws SQLException{
            Notification model = new Notification();
            model.setId(rs.getLong("id"));
            model.setUserId(rs.getInt("user_id"));
            model.setContent(rs.getString("content"));
            model.setMarked(rs.getInt("marked"));
            model.setRequestId(rs.getInt("request_id"));
            return model;
        }
    }
}
