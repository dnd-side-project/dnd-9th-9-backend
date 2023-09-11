package com.dnd.Exercise.domain.notification.service;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.notification.dto.response.FindFieldNotificationsRes;
import com.dnd.Exercise.domain.notification.dto.response.FindUserNotificationsRes;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.dnd.Exercise.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    void sendByTokens(List<FcmToken> fcmTokens, NotificationDto notificationDto);

    FindUserNotificationsRes findUserNotifications(User user, Pageable pageable);

    FindFieldNotificationsRes findFieldNotifications(User user, Long id, Pageable pageable);

    void readNotification(User user, Long id);

    void readAllNotifications(User user);
}
