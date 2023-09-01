package com.dnd.Exercise.domain.notification.service;

import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_FOUND;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.notification.entity.Notification;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.dnd.Exercise.domain.notification.entity.NotificationTopic;
import com.dnd.Exercise.domain.notification.repository.NotificationRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotificationServiceImpl implements NotificationService{
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Transactional
    @Override
    public void cheerMember(User user, Long id){
        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));

        List<FcmToken> fcmTokens = targetUser.getFcmTokens();

        NotificationDto notificationDto = NotificationDto.builder()
                .topic(NotificationTopic.CHEER)
                .from(user.getName())
                .build();

        ApnsConfig apnsConfig = notificationDto.toDefaultApnsConfig();

        try {
            for (FcmToken token : fcmTokens) {

                Message mes = Message.builder()
                        .setApnsConfig(apnsConfig)
                        .setToken(String.valueOf(token.getToken()))
                        .build();

                firebaseMessaging.send(mes);
            }

            Notification notification = notificationDto.toEntity(targetUser, null);
            notificationRepository.save(notification);

        }catch (FirebaseMessagingException e) {
            log.info(String.valueOf(e));
            throw new BusinessException(BAD_REQUEST);
        }

    }
}
