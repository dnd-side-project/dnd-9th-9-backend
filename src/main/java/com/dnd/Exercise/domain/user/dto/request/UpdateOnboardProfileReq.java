package com.dnd.Exercise.domain.user.dto.request;

import com.dnd.Exercise.domain.user.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateOnboardProfileReq {
    private int weight;

    private int height;

    private Gender gender;

    private Boolean isAppleLinked;

    private int calorieGoal;
}
