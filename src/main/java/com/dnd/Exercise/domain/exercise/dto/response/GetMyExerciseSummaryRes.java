package com.dnd.Exercise.domain.exercise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMyExerciseSummaryRes {

    private int totalBurnedCalorie;

    private int totalExerciseCalorie;

    private int totalExerciseTimeMinute;

    private int totalRecordCount;
}
