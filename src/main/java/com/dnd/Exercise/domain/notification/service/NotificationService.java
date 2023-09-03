package com.dnd.Exercise.domain.notification.service;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import java.util.List;

public interface NotificationService {

    void sendByTokens(List<FcmToken> fcmTokens, NotificationDto notificationDto);
}
