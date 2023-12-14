package com.dnd.Exercise.domain.notification.entity;

import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.*;

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
    public NotificationDto(NotificationTopic topic,
            Field field, String name, NotificationType notificationType){
        this.title = topic.getTitle();
        this.notificationType = notificationType;
        if (topic == CHEER){
            this.content = name + "님이 응원해요❣";
        }
        else if (topic == ALERT) {
            this.content = name + "님이 " + field.getName() + " 팀을 깨웠어요💡";
        }
        else if (topic == EJECT) {
            if (NotificationType.FIELD.equals(notificationType)){
                this.field = field;
                this.content = field.getName() + "에서 " + name + "님을 내보냈습니다";
            } else {
                this.content = field.getName() + "에서 퇴출되었습니다";
            }
        }
        else if (topic == EXIT){
            this.field = field;
            this.content = name + "님이 " + field.getName() + "을 나갔습니다";
        }
        else if (topic == CHANGE_LEADER){
            this.field = field;
            this.content = name + "님이 " + field.getName() + "의 방장이 되었습니다";
        }
        else if (topic == UPDATE_INFO){
            this.field = field;
            this.content = field.getName() + "의 정보가 수정되었습니다";
        }
        else if (topic == BATTLE_ACCEPT){
            this.field = field;
            this.content = name + "팀과 배틀이 성사되었습니다";
        }
        else if (topic == TEAM_ACCEPT){
            if (NotificationType.FIELD.equals(notificationType)){
                this.field = field;
                this.content = name + "님이 입장했습니다";
            } else{
                this.content = field.getName() + "팀에 소속되었습니다";
            }
        }
    }

    //유저 알림
    public Notification toEntity(User user){
        return Notification.builder()
                .title(title)
                .content(content)
                .notificationType(notificationType)
                .user(user)
                .field(field)
                .build();
    }

    //필드 알림
    public Notification toEntity(){
        return Notification.builder()
                .title(title)
                .content(content)
                .notificationType(notificationType)
                .user(null)
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
