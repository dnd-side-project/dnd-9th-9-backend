package com.dnd.Exercise.domain.exercise.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMyExerciseSummaryRes {

    private int totalBurnedCalorie;

    private int totalExerciseCalorie;

    private int totalExerciseTimeMinute;

    private int totalRecordCount;
}
