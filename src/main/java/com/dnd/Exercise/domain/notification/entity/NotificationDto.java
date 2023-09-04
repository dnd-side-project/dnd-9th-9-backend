package com.dnd.Exercise.domain.notification.entity;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import lombok.Builder;
import lombok.Data;

@Data
public class NotificationDto {
    private String title;
    private String content;
    private NotificationType notificationType;
    private Field field;

    @Builder
    public NotificationDto(NotificationTopic topic, String from, String to, Field field){
        this.title = topic.getTitle();
        this.field = field;
        if (topic == NotificationTopic.CHEER){
            this.content = from + "님이 응원해요❣";
            this.notificationType = NotificationType.USER;
        } else if (topic == NotificationTopic.ALERT) {
            this.content = from + "님이 " + field.getName() + " 팀을 깨웠어요💡";
            this.notificationType = NotificationType.FIELD;
        }
    }

    public Notification toEntity(User user){
        return Notification.builder()
                .title(title)
                .content(content)
                .notificationType(notificationType)
                .user(user)
                .field(field)
                .build();
    }

    public ApnsConfig toDefaultApnsConfig() {
        return ApnsConfig.builder()
                .setAps(
                        Aps.builder()
                                .setAlert(
                                        ApsAlert.builder()
                                                .setTitle(title)
                                                .setBody(content)
                                                .build()
                                )
                                .setCategory("NOTIFICATION_CLICK")
                                .setSound("default")
                                .build()
                )
                .build();
    }
}
