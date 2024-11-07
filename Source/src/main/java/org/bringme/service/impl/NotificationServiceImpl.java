package org.bringme.service.impl;

import org.bringme.dto.NotificationDTO;
import org.bringme.model.Notification;
import org.bringme.repository.NotificationRepository;
import org.bringme.service.NotificationService;
import org.bringme.utils.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final Converter converter;

    public NotificationServiceImpl(NotificationRepository notificationRepository, Converter converter) {
        this.notificationRepository = notificationRepository;
        this.converter = converter;
    }

    @Override
    public List<NotificationDTO> getAll(int userId) {
        List<Notification> allModel = notificationRepository.getAll(userId);
        if(allModel.isEmpty()){
            return List.of();
        }
        List<NotificationDTO> response = new ArrayList<>();
        for(Notification model : allModel){
            NotificationDTO dto = converter.notificationToDTO(model);
            response.add(dto);
        }
        return response;
    }

    @Override
    public List<NotificationDTO> getNotMarked(int userId) {
        List<Notification> notMarkedModel = notificationRepository.getNotMarked(userId);
        if(notMarkedModel.isEmpty()){
            return List.of();
        }
        List<NotificationDTO> response = new ArrayList<>();
        for(Notification model : notMarkedModel){
            NotificationDTO dto = converter.notificationToDTO(model);
            response.add(dto);
        }
        return response;
    }

    @Override
    public void saveNotification(int userId, String content, int requestId) {
        notificationRepository.save(userId, content, requestId);
    }

    @Override
    public void markOneAsRead(int userId, Long id) {
        notificationRepository.markOneAsRead(userId, id);
    }

    @Override
    public void markAllAsRead(int userId) {
        notificationRepository.markAllAsRead(userId);
    }
}
