package com.dnd.Exercise.domain.notification.dto.response;

import lombok.Data;

@Data
public class UserNotificationDto {
    private Long id;

    private Long matchId;

    private String content;

    private Boolean isRead;
}
