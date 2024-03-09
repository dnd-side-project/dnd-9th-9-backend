package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
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

    public static GetFieldExerciseSummaryRes of(List<Integer> summary){
        return GetFieldExerciseSummaryRes.builder()
                .totalBurnedCalorie(summary.get(0))
                .goalAchievedCount(summary.get(1))
                .totalBurnedCalorie(summary.get(2))
                .totalExerciseTimeMinute(summary.get(3))
                .build();
    }

    public void changeFrom(WinStatus winStatus, String opponentFieldName){
        this.winStatus = winStatus;
        this.opponentFieldName = opponentFieldName;
    }
}
