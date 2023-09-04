package com.dnd.Exercise.domain.notification.service;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.fcmToken.repository.FcmTokenRepository;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.SendResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotificationServiceImpl implements NotificationService{
    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenRepository fcmTokenRepository;


    @Transactional
    @Override
    public void sendByTokens(List<FcmToken> tokens, NotificationDto notificationDto) {

        ApnsConfig apnsConfig = notificationDto.toDefaultApnsConfig();

        List<Message> messages = tokens.stream().map(token ->
                makeMessage(apnsConfig, token)).collect(Collectors.toList());

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

    private static Message makeMessage(ApnsConfig apnsConfig, FcmToken token) {
        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setToken(String.valueOf(token.getToken()))
                .build();
    }
}
