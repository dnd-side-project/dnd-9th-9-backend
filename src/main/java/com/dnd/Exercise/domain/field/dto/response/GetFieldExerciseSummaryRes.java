package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.WinStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetFieldExerciseSummaryRes {

    private int totalRecordCount;

    private int goalAchievedCount;

    private int totalBurnedCalorie;

    private int totalExerciseTimeMinute;

    private String opponentFieldName;

    private WinStatus winStatus;
}