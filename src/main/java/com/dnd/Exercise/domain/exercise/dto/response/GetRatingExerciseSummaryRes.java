package com.dnd.Exercise.domain.exercise.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetRatingExerciseSummaryRes {

    private int totalRecordCount;

    private int goalAchievedCount;

    private int totalBurnedCalorie;

    private int totalExerciseTimeMinute;
}
