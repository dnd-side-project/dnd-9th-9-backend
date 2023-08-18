package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.WinStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
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

    public GetFieldExerciseSummaryRes(int totalRecordCount, int goalAchievedCount,
            int totalBurnedCalorie, int totalExerciseTimeMinute) {
        this.totalRecordCount = totalRecordCount;
        this.goalAchievedCount = goalAchievedCount;
        this.totalBurnedCalorie = totalBurnedCalorie;
        this.totalExerciseTimeMinute = totalExerciseTimeMinute;
        this.opponentFieldName = null;
        this.winStatus = null;
    }
}
