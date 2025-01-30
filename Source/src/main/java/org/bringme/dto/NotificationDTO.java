package org.bringme.dto;

/**
 *
 * @param id Unique id Primary Key
 * @param userId User who has the notification
 * @param content About what the notification it is
 * @param marked Read \ Unread
 * @param requestId For which request it related if exists
 */
public record NotificationDTO(Long id, int userId, String content, int marked, int requestId){}
