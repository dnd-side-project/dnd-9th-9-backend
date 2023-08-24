package com.dnd.Exercise.domain.exercise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCalorieStateRes {

    private int burnedCalorie;

    private int goalCalorie;
}
