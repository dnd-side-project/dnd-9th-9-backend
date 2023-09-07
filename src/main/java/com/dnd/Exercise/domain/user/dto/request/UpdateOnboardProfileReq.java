package com.dnd.Exercise.domain.user.dto.request;

import com.dnd.Exercise.domain.user.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateOnboardProfileReq {
    @NotNull
    private double weight;

    @NotNull
    private double height;

    @NotNull
    private Gender gender;

    @NotNull
    private Boolean isAppleLinked;

    private Integer calorieGoal;
}
