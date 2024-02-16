package com.dnd.Exercise.domain.notification.event;

import static com.dnd.Exercise.global.common.Constants.ASYNC_NOTIFICATION;
import static java.util.stream.Collectors.toList;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.fcmToken.repository.FcmTokenRepository;
import com.dnd.Exercise.domain.notification.entity.Notification;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.dnd.Exercise.domain.notification.entity.NotificationType;
import com.dnd.Exercise.domain.notification.repository.NotificationRepository;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Async(ASYNC_NOTIFICATION)
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @EventListener
    public void handleNotificationEvent(NotificationEvent notificationEvent) {
        List<User> users = notificationEvent.getUsers();
        NotificationDto notificationDto = notificationEvent.getNotificationDto();

        List<FcmToken> fcmTokens = fcmTokenRepository.findByUserIn(users);

        if (fcmTokens.size() != 0) notificationService.sendByTokens(fcmTokens, notificationDto);

        if (NotificationType.FIELD.equals(notificationDto.getNotificationType())){
            notificationRepository.save(notificationDto.toEntity());
        }
        else {
            List<Notification> notifications = users.stream()
                    .map(notificationDto::toEntity).collect(toList());
            notificationRepository.saveAll(notifications);
        }
    }


}
