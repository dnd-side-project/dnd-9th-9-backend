package com.dnd.Exercise.domain.userField.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleStatusDto {

    private String name;

    private Integer totalRecordCount;

    private Integer goalAchievedCount;

    private Integer totalBurnedCalorie;

    private Integer totalExerciseTimeMinute;
}
