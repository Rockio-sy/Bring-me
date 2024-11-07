package org.bringme.dto;

public record NotificationDTO(Long id, int userId, String content, int marked, int requestId){}
