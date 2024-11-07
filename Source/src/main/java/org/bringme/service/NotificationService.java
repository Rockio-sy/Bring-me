package org.bringme.service;

import org.bringme.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getAll(Long userId);
    List<NotificationDTO> getNotMarked(Long userId);
}
