package com.dnd.Exercise.domain.notification.dto.response;

import lombok.Data;

@Data
public class UserNotificationDto {
    private Long id;

    private Long fieldId;

    private String content;

    private Boolean isRead;
}
