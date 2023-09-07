package com.dnd.Exercise.domain.user.dto.request;

import com.dnd.Exercise.domain.user.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateOnboardProfileReq {
    @NotNull
    private int weight;

    @NotNull
    private int height;

    @NotNull
    private Gender gender;

    @NotNull
    private Boolean isAppleLinked;

    private Integer calorieGoal;
}
