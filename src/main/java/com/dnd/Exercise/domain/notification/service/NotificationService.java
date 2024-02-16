package com.dnd.Exercise.domain.notification.service;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.notification.dto.response.FindFieldNotificationsRes;
import com.dnd.Exercise.domain.notification.dto.response.FindUserNotificationsRes;
import com.dnd.Exercise.domain.notification.entity.NotificationTopic;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void sendFieldNotification(NotificationTopic topic, Field field);
    void sendFieldNotification(NotificationTopic topic, Field field, String name);
    void sendUserNotification(NotificationTopic topic, Field field, User user);
    void sendUserNotification(NotificationTopic topic, String name, User user);
    void readNotification(User user, Long id);
    void readAllNotifications(User user);
    FindUserNotificationsRes findUserNotifications(User user, Pageable pageable);
    FindFieldNotificationsRes findFieldNotifications(User user, Long id, Pageable pageable);

}
