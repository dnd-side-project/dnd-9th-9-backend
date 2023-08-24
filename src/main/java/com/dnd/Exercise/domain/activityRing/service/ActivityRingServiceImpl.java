package com.dnd.Exercise.domain.activityRing.service;

import com.dnd.Exercise.domain.activityRing.dto.ActivityRingMapper;
import com.dnd.Exercise.domain.activityRing.dto.request.UpdateActivityRingReq;
import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityRingServiceImpl implements ActivityRingService {

    private final ActivityRingRepository activityRingRepository;
    private final ActivityRingMapper activityRingMapper;

    @Override
    @Transactional
    public void updateActivityRing(UpdateActivityRingReq updateActivityRingReq, User user) {
        ActivityRing activityRing = activityRingRepository.findByDateAndUserId(updateActivityRingReq.getDate(), user.getId()).orElse(null);

        if (activityRing == null) {
            activityRing = updateActivityRingReq.toEntityWithUser(user);
            activityRingRepository.save(activityRing);
        }
        else {
            activityRingMapper.updateFromDto(updateActivityRingReq,activityRing);
        }
    }
}
