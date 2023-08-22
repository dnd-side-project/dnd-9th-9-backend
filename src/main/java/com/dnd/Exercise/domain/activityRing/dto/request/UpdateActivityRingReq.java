package com.dnd.Exercise.domain.activityRing.dto.request;

import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateActivityRingReq {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private int burnedCalorie;

    public ActivityRing toEntityWithUser(User user) {
        return ActivityRing.builder()
                .user(user)
                .date(date)
                .burnedCalorie(burnedCalorie)
                .isGoalAchieved(false)
                .build();
    }
}
