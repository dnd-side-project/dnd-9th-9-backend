package com.dnd.Exercise.domain.field.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindFieldResultDto {
    private String name;

    private String profileImg;

    private Integer totalRecordCount;

    private Integer goalAchievedCount;

    private Integer totalBurnedCalorie;

    private Integer totalExerciseTimeMinute;
}
