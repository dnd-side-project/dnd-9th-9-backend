package com.dnd.Exercise.domain.exercise.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMyExerciseSummaryRes {
    @ApiModelProperty(value = "총 소비 칼로리")
    private int totalBurnedCalorie;

    @ApiModelProperty(value = "총 운동 칼로리")
    private int totalExerciseCalorie;

    @ApiModelProperty(value = "총 운동 시간")
    private int totalExerciseTimeMinute;

    @ApiModelProperty(value = "오늘 하루의 총 기록 횟수")
    private int totalRecordCount;
}
