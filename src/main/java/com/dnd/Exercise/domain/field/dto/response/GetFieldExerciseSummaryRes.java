package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.WinStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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

    public GetFieldExerciseSummaryRes(List<Integer> summary) {
        this.totalRecordCount = summary.get(0);
        this.goalAchievedCount = summary.get(1);
        this.totalBurnedCalorie = summary.get(2);
        this.totalExerciseTimeMinute = summary.get(3);
        this.opponentFieldName = null;
        this.winStatus = null;
    }
}
