package com.dnd.Exercise.domain.notification.service;

import static com.dnd.Exercise.domain.notification.entity.NotificationType.FIELD;
import static com.dnd.Exercise.domain.notification.entity.NotificationType.USER;
import static com.dnd.Exercise.global.common.Constants.ASYNC_NOTIFICATION;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FORBIDDEN;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOTIFICATION_NOT_FOUND;
import static java.util.stream.Collectors.toList;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.fcmToken.repository.FcmTokenRepository;
import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.notification.dto.NotificationMapper;
import com.dnd.Exercise.domain.notification.dto.response.FieldNotificationDto;
import com.dnd.Exercise.domain.notification.dto.response.FindFieldNotificationsRes;
import com.dnd.Exercise.domain.notification.dto.response.FindUserNotificationsRes;
import com.dnd.Exercise.domain.notification.dto.response.UserNotificationDto;
import com.dnd.Exercise.domain.notification.entity.Notification;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.dnd.Exercise.domain.notification.entity.NotificationTopic;
import com.dnd.Exercise.domain.notification.entity.NotificationType;
import com.dnd.Exercise.domain.notification.repository.NotificationRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.error.exception.BusinessException;
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
import org.springframework.scheduling.annotation.Async;
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
    private final FieldBusiness fieldBusiness;

    @Override
    @Transactional
    @Async(ASYNC_NOTIFICATION)
    public void sendFieldNotification(NotificationTopic topic, Field field){
        NotificationDto notificationDto = NotificationDto.builder()
                .topic(topic)
                .field(field)
                .notificationType(NotificationType.FIELD)
                .build();

        List<User> members = fieldBusiness.getMembers(field.getId());
        List<FcmToken> fcmTokens = fcmTokenRepository.findByUserIn(members);
        sendNotificationAndSave(notificationDto, fcmTokens);
    }

    @Override
    @Transactional
    @Async(ASYNC_NOTIFICATION)
    public void sendFieldNotification(NotificationTopic topic, Field field, String name){
        NotificationDto notificationDto = NotificationDto.builder()
                .topic(topic)
                .field(field)
                .notificationType(NotificationType.FIELD)
                .name(name)
                .build();

        List<User> members = fieldBusiness.getMembers(field.getId());
        List<FcmToken> fcmTokens = fcmTokenRepository.findByUserIn(members);
        sendNotificationAndSave(notificationDto, fcmTokens);
    }

    @Override
    @Transactional
    @Async(ASYNC_NOTIFICATION)
    public void sendUserNotification(NotificationTopic topic, Field field, User user){
        NotificationDto notificationDto = NotificationDto.builder()
                .topic(topic)
                .field(field)
                .notificationType(NotificationType.USER)
                .name(user.getName())
                .build();

        List<FcmToken> fcmTokens = fcmTokenRepository.findByUser(user);
        sendNotificationAndSave(notificationDto, fcmTokens);
    }

    @Override
    @Transactional
    @Async(ASYNC_NOTIFICATION)
    public void sendUserNotification(NotificationTopic topic, String name, User user) {
        NotificationDto notificationDto = NotificationDto.builder()
                .topic(topic)
                .notificationType(NotificationType.USER)
                .name(name)
                .build();

        List<FcmToken> fcmTokens = fcmTokenRepository.findByUser(user);
        sendNotificationAndSave(notificationDto, fcmTokens);
    }

    @Override
    @Transactional
    public void readNotification(User user, Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOTIFICATION_NOT_FOUND));

        isMyNotification(user, notification);
        notification.isReadTrue();
    }

    @Override
    @Transactional
    public void readAllNotifications(User user) {
        notificationRepository.bulkIsRead(user.getId());
    }

    @Override
    public FindUserNotificationsRes findUserNotifications(User user, Pageable pageable) {
        Page<Notification> queryResult = notificationRepository.findByUserAndNotificationType(user, USER, pageable);

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

    @Override
    public FindFieldNotificationsRes findFieldNotifications(User user, Long id, Pageable pageable) {
        Field field = fieldBusiness.getField(id);
        fieldBusiness.validateIsMember(user, field);

        Page<Notification> queryResult = notificationRepository.findByUserAndNotificationType(user, FIELD, pageable);

        List<Notification> notifications = queryResult.getContent();
        Long totalCount = queryResult.getTotalElements();

        List<FieldNotificationDto> notificationInfos = notifications.stream()
                .map(notificationMapper::toFieldNotificationDto).collect(toList());

        return FindFieldNotificationsRes.builder()
                .notificationInfos(notificationInfos)
                .totalCount(totalCount)
                .currentPageNumber(pageable.getPageNumber())
                .currentPageSize(pageable.getPageSize())
                .build();
    }

    private void sendByTokens(List<FcmToken> tokens, NotificationDto notificationDto) {
        List<Message> messages = getMessages(tokens, notificationDto);
        BatchResponse response;
        try {
            response = firebaseMessaging.sendAll(messages);
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                deleteFailedTokens(responses, tokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }
    }

    private void deleteFailedTokens(List<SendResponse> responses, List<FcmToken> tokens){
        List<FcmToken> failedTokens = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            if (!responses.get(i).isSuccessful())
                failedTokens.add(tokens.get(i));
        }
        fcmTokenRepository.deleteAll(failedTokens);
        log.info("List of tokens are not valid FCM token : " + failedTokens);
    }

    private void sendNotificationAndSave(NotificationDto notificationDto, List<FcmToken> fcmTokens) {
        if (fcmTokens.size() != 0) sendByTokens(fcmTokens, notificationDto);
        notificationRepository.save(notificationDto.toEntity());
    }

    private List<Message> getMessages(List<FcmToken> tokens, NotificationDto notificationDto) {
        ApnsConfig apnsConfig = notificationDto.toDefaultApnsConfig();
        return tokens.stream().map(token ->
                makeMessage(apnsConfig, token)).collect(toList());
    }

    private Message makeMessage(ApnsConfig apnsConfig, FcmToken token) {
        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setToken(String.valueOf(token.getToken()))
                .build();
    }

    private void isMyNotification(User user, Notification notification) {
        if(!user.getId().equals(notification.getUser().getId())){
            throw new BusinessException(FORBIDDEN);
        }
    }
}
