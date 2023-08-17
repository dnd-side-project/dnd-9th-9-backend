package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.WinStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFieldExerciseSummaryRes {

    private int totalRecordCount;

    private int goalAchievedCount;

    private int totalBurnedCalorie;

    private int totalExerciseTimeMinute;

    private String opponentFieldName;

    private WinStatus winStatus;
}
