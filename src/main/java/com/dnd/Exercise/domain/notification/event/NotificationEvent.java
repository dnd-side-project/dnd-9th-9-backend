package com.dnd.Exercise.domain.notification.event;

import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.dnd.Exercise.domain.user.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class NotificationEvent {
    private final List<User> users;
    private final NotificationDto notificationDto;
}
