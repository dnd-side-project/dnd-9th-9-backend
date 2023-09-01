package com.dnd.Exercise.domain.exercise.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRecentsRes {
    @ApiModelProperty(value = "오늘 하루 총 운동 시간")
    private int totalExerciseMinute;

    @ApiModelProperty(value = "오늘 하루 총 소모 칼로리")
    private int totalBurnedCalorie;

    @ApiModelProperty(value = "오늘 하루 가장 많이 한 운동 리스트")
    List<RecentSportsDto> recentSports;
}
