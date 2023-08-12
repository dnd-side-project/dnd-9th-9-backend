package com.dnd.Exercise.domain.exercise.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCalorieStateRes {

    private int burnedCalorie;

    private int goalCalorie;
}
