package com.dnd.Exercise.domain.match.dto.response;

import com.dnd.Exercise.domain.match.entity.WinStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMatchExerciseSummaryRes {

    private int totalRecordCount;

    private int goalAchievedCount;

    private int totalBurnedCalorie;

    private int totalExerciseTimeMinute;

    private String opponentMatchName;

    private WinStatus winStatus;
}
