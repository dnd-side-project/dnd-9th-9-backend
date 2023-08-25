package com.dnd.Exercise.domain.userField.dto.response;

import java.util.List;
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

    public BattleStatusDto(String name, List<Integer> score){
        this.name = name;
        this.totalRecordCount = score.get(0);
        this.goalAchievedCount = score.get(1);
        this.totalBurnedCalorie = score.get(2);
        this.totalExerciseTimeMinute = score.get(3);
    }
}
