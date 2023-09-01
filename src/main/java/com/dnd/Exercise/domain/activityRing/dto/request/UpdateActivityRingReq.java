package com.dnd.Exercise.domain.activityRing.dto.request;

import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateActivityRingReq {

    @NotNull(message = "날짜를 입력해주세요.")
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "칼로리를 입력해주세요.")
    private Integer burnedCalorie;

    public ActivityRing toEntityWithUser(User user) {
        return ActivityRing.builder()
                .user(user)
                .date(date)
                .burnedCalorie(burnedCalorie)
                .isGoalAchieved(false)
                .build();
    }
}
