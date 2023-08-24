package com.dnd.Exercise.domain.activityRing.service;

import com.dnd.Exercise.domain.activityRing.dto.request.UpdateActivityRingReq;
import com.dnd.Exercise.domain.user.entity.User;

public interface ActivityRingService {
    void updateActivityRing(UpdateActivityRingReq updateActivityRingReq, User user);
}
