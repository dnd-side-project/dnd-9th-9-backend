package com.dnd.Exercise.domain.activityRing.service;

import com.dnd.Exercise.domain.activityRing.dto.ActivityRingMapper;
import com.dnd.Exercise.domain.activityRing.dto.request.UpdateActivityRingReq;
import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityRingServiceImpl implements ActivityRingService {

    private final ActivityRingRepository activityRingRepository;
    private final ActivityRingMapper activityRingMapper;

    @Override
    @Transactional
    public void updateActivityRing(UpdateActivityRingReq updateActivityRingReq, User user) {
        validateIsAppleLinked(user);

        ActivityRing activityRing = getActivityRingOfDate(updateActivityRingReq.getDate(), user.getId());
        if (activityRing == null) {
            activityRing = updateActivityRingReq.toEntityWithUser(user);
            activityRingRepository.save(activityRing);
        }
        else {
            activityRingMapper.updateFromDto(updateActivityRingReq,activityRing);
        }

        checkIsGoalAchieved(activityRing,user);
    }

    private ActivityRing getActivityRingOfDate(LocalDate date, Long userId) {
        return activityRingRepository.findByDateAndUserId(date, userId).orElse(null);
    }

    private void validateIsAppleLinked(User user) {
        if (!user.getIsAppleLinked()) {
            throw new BusinessException(ErrorCode.ACTIVITY_RING_UPDATE_UNAVAILABLE);
        }
    }

    private void checkIsGoalAchieved(ActivityRing activityRing, User user) {
        if (activityRing.getBurnedCalorie() == user.getCalorieGoal()) {
            activityRing.updateIsGoalAchieved(true);
        }
    }
}
