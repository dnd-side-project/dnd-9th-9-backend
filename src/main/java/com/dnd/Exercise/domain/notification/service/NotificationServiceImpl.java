package com.dnd.Exercise.domain.notification.service;

import static com.dnd.Exercise.domain.notification.entity.NotificationType.USER;
import static java.util.stream.Collectors.toList;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.fcmToken.repository.FcmTokenRepository;
import com.dnd.Exercise.domain.notification.dto.NotificationMapper;
import com.dnd.Exercise.domain.notification.dto.response.FindUserNotificationsRes;
import com.dnd.Exercise.domain.notification.dto.response.UserNotificationDto;
import com.dnd.Exercise.domain.notification.entity.Notification;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.dnd.Exercise.domain.notification.repository.NotificationRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.SendResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotificationServiceImpl implements NotificationService{
    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;


    @Transactional
    @Override
    public void sendByTokens(List<FcmToken> tokens, NotificationDto notificationDto) {

        ApnsConfig apnsConfig = notificationDto.toDefaultApnsConfig();

        List<Message> messages = tokens.stream().map(token ->
                makeMessage(apnsConfig, token)).collect(toList());

        BatchResponse response;
        try {
            response = firebaseMessaging.sendAll(messages);
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<FcmToken> failedTokens = new ArrayList<>();

                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(tokens.get(i));
                    }
                }
                fcmTokenRepository.deleteAll(failedTokens);
                log.info("List of tokens are not valid FCM token : " + failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }
    }

    @Override
    public FindUserNotificationsRes findUserNotifications(User user, Pageable pageable) {
        Page<Notification> queryResult = notificationRepository
                .findByUserAndNotificationType(user, USER, pageable);

        List<Notification> notifications = queryResult.getContent();
        Long totalCount = queryResult.getTotalElements();

        List<UserNotificationDto> notificationInfos = notifications.stream()
                .map(notificationMapper::toUserNotificationDto).collect(toList());

        return FindUserNotificationsRes.builder()
                .notificationInfos(notificationInfos)
                .totalCount(totalCount)
                .currentPageNumber(pageable.getPageNumber())
                .currentPageSize(pageable.getPageSize())
                .build();
    }

    private static Message makeMessage(ApnsConfig apnsConfig, FcmToken token) {
        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setToken(String.valueOf(token.getToken()))
                .build();
    }
}
