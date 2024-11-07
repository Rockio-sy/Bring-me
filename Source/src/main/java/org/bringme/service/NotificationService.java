package org.bringme.service;

import org.bringme.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getAll(int userId);
    List<NotificationDTO> getNotMarked(int userId);

    void saveNotification(int userId, String content, int requestId);

    void markOneAsRead(int userId, Long id);

    void markAllAsRead(int i);
}
