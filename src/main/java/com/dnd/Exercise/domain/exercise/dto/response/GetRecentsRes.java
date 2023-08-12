package com.dnd.Exercise.domain.exercise.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetRecentsRes {
    private int totalExerciseMinute;
    private int totalBurnedCalorie;

    List<RecentSportsDto> recentSports;
}
