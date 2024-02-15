package com.dnd.Exercise.domain.notification.service;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.notification.dto.response.FindFieldNotificationsRes;
import com.dnd.Exercise.domain.notification.dto.response.FindUserNotificationsRes;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.dnd.Exercise.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void sendNotificationAndSave(List<User> users, NotificationDto notificationDto);
    void sendNotificationAndSave(User user, NotificationDto notificationDto);
    void readNotification(User user, Long id);
    void readAllNotifications(User user);
    FindUserNotificationsRes findUserNotifications(User user, Pageable pageable);
    FindFieldNotificationsRes findFieldNotifications(User user, Long id, Pageable pageable);

}
