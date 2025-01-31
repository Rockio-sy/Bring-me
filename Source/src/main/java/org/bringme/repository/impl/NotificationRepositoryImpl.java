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
import java.util.Optional;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    private final JdbcTemplate jdbcTemplate;

    public NotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * List all notifications of specific user
     * @param userId who own the  notifications
     * @return List of notifications
     */
    @Override
    public List<Notification> getAll(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ?";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), userId);
    }


    /**
     * List all unread notifications of specific user
     * @param userId who own the  notifications
     * @return List of unread notifications
     */
    @Override
    public List<Notification> getNotMarked(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND marked = 0";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), userId);
    }

    /**
     * Save new notification into the database
     * @param userId Who own the notifications
     * @param content about what talk the notification
     * @param requestId id of request on that notifications will be sent
     */
    @Override
    public void save(int userId, String content, int requestId) {
        String sql = "INSERT INTO notifications (user_id, content, request_id)" +
                "VALUES" +
                "(?, ?, ?)";
        jdbcTemplate.update(sql, userId, content, requestId);
    }

    /**
     * Mark one notification as read
     * @param userId Who own the notification
     * @param id of the notification
     */
    @Override
    public void markOneAsRead(int userId, Long id) {
        String sql = "UPDATE notifications SET marked = 1 WHERE id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
    }

    /**
     * Mark all notifications as read
     * @param userId who own the notifications
     */
    @Override
    public void markAllAsRead(int userId) {
        String sql = "UPDATE notifications SET marked = 1 WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    /**
     * Get specific notification from database
     * @param id of the notification
     * @return notification
     */
    @Override
    public Optional<Notification> getById(Long id) {
        String sql = "SELECT * FROM notifications WHERE id = ?";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), id).stream().findFirst();
    }

    /**
     * Row mapper to map database data with java object
     */
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
