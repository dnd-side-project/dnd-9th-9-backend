package com.dnd.Exercise.domain.notification.dto;

import com.dnd.Exercise.domain.notification.dto.response.UserNotificationDto;
import com.dnd.Exercise.domain.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "notification.field.id", target = "fieldId")
    UserNotificationDto toUserNotificationDto(Notification notification);
}
