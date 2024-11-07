package org.bringme.repository;

import org.bringme.model.Notification;

import java.util.List;

public interface NotificationRepository {
    List<Notification> getAll(Long userId);
    List<Notification> getNotMarked(Long userId);
}
