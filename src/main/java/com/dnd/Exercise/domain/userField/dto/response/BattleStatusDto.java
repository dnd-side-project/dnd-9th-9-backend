package com.dnd.Exercise.domain.userField.dto.response;

import lombok.Data;

@Data
public class BattleStatusDto {

    private String name;

    private int totalRecordCount;

    private int goalAchievedCount;

    private int totalBurnedCalorie;

    private int totalExerciseTimeMinute;
}
