package org.bringme.repository;

import org.bringme.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    List<Notification> getAll(int userId);
    List<Notification> getNotMarked(int userId);

    void save(int userId, String content, int requestId);

    void markOneAsRead(int userId, Long id);

    void markAllAsRead(int userId);

    Optional<Notification> getById(Long id);
}
