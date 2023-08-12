package com.dnd.Exercise.domain.notification.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class FindAllNotificationsRes {

    private List<UserNotificationDto> notificationInfos;

    private Long totalCount;
}
