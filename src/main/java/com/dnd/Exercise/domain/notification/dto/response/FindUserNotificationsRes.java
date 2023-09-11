package com.dnd.Exercise.domain.notification.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindUserNotificationsRes {

    private List<UserNotificationDto> notificationInfos;

    private Long totalCount;

    private int currentPageSize;

    private int currentPageNumber;
}
