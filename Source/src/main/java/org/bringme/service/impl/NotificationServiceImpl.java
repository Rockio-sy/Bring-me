package org.bringme.service.impl;

import org.bringme.dto.NotificationDTO;
import org.bringme.exceptions.CustomException;
import org.bringme.model.Notification;
import org.bringme.repository.NotificationRepository;
import org.bringme.service.NotificationService;
import org.bringme.utils.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final Converter converter;

    public NotificationServiceImpl(NotificationRepository notificationRepository, Converter converter) {
        this.notificationRepository = notificationRepository;
        this.converter = converter;
    }

    /**
     * Retrieves all notifications for the given user.
     * If no notifications are found, it throws a {@link CustomException} with a {@link HttpStatus#NO_CONTENT} status.
     *
     * @param userId The ID of the user for whom notifications are being retrieved.
     * @return A list of {@link NotificationDTO} objects representing the notifications of the user.
     * @throws CustomException If no notifications are found for the given user.
     */
    @Override
    public List<NotificationDTO> getAll(int userId) {
        List<Notification> allModel = notificationRepository.getAll(userId);
        if (allModel.isEmpty()) {
            throw new CustomException("No content", HttpStatus.NO_CONTENT);
        }
        List<NotificationDTO> response = new ArrayList<>();
        for (Notification model : allModel) {
            NotificationDTO dto = converter.notificationToDTO(model);
            response.add(dto);
        }
        return response;
    }

    /**
     * Retrieves all unread (not marked) notifications for the given user.
     * If no unread notifications are found, it throws a {@link CustomException} with a {@link HttpStatus#NO_CONTENT} status.
     *
     * @param userId The ID of the user for whom unread notifications are being retrieved.
     * @return A list of {@link NotificationDTO} objects representing the unread notifications of the user.
     * @throws CustomException If no unread notifications are found for the given user.
     */
    @Override
    public List<NotificationDTO> getNotMarked(int userId) {
        List<Notification> notMarkedModel = notificationRepository.getNotMarked(userId);
        if (notMarkedModel.isEmpty()) {
            throw new CustomException("No content", HttpStatus.NO_CONTENT);
        }
        List<NotificationDTO> response = new ArrayList<>();
        for (Notification model : notMarkedModel) {
            NotificationDTO dto = converter.notificationToDTO(model);
            response.add(dto);
        }
        return response;
    }

    /**
     * Saves a notification for the given user.
     * This method stores the notification content and associates it with a specific request.
     *
     * @param userId    The ID of the user for whom the notification is being created.
     * @param content   The content of the notification.
     * @param requestId The ID of the request associated with the notification.
     */
    @Override
    public void saveNotification(int userId, String content, int requestId) {
        notificationRepository.save(userId, content, requestId);
    }

    /**
     * Marks a specific notification as read for the given user.
     * If the notification is not found or the notification does not belong to the user,
     * a {@link CustomException} is thrown.
     *
     * @param userId The ID of the user marking the notification as read.
     * @param id     The ID of the notification to mark as read.
     * @throws CustomException If the notification is not found or does not belong to the user.
     */
    @Override
    public void markOneAsRead(int userId, Long id) {
        Optional<Notification> noty = notificationRepository.getById(id);
        if (noty.isEmpty()) {
            throw new CustomException("No such notification", HttpStatus.BAD_REQUEST);
        }
        if (!(noty.get().getUserId() == userId)) {
            throw new CustomException("User doesn't belong to this notification", HttpStatus.BAD_REQUEST);
        }

        notificationRepository.markOneAsRead(userId, id);
    }

    /**
     * Marks all notifications as read for the given user.
     *
     * @param userId The ID of the user marking all notifications as read.
     */
    @Override
    public void markAllAsRead(int userId) {
        notificationRepository.markAllAsRead(userId);
    }
}
