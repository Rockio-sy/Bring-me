package org.bringme.service.impl;

import org.bringme.dto.NotificationDTO;
import org.bringme.model.Notification;
import org.bringme.repository.NotificationRepository;
import org.bringme.service.NotificationService;
import org.bringme.service.exceptions.CustomException;
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

    @Override
    public List<NotificationDTO> getAll(int userId) {
        List<Notification> allModel = notificationRepository.getAll(userId);
        if(allModel.isEmpty()){
            throw new CustomException("No content", HttpStatus.NO_CONTENT);
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
            throw new CustomException("No content", HttpStatus.NO_CONTENT);
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
        Optional<Notification> noty = notificationRepository.getById(id);
        if(noty.isEmpty()){
            throw new CustomException("No such notification", HttpStatus.BAD_REQUEST);
        }
        if(!(noty.get().getUserId() == userId)){
            throw new CustomException("User doesn't belong to this notification", HttpStatus.BAD_REQUEST);
        }

        notificationRepository.markOneAsRead(userId, id);
    }

    @Override
    public void markAllAsRead(int userId) {
        notificationRepository.markAllAsRead(userId);
    }
}
