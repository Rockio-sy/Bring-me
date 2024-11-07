package org.bringme.repository;

import org.bringme.model.Notification;

import java.util.List;

public interface NotificationRepository {
    List<Notification> getAll(int userId);
    List<Notification> getNotMarked(int userId);

    void save(int userId, String content, int requestId);

    void markOneAsRead(int userId, Long id);

    void markAllAsRead(int userId);
}
