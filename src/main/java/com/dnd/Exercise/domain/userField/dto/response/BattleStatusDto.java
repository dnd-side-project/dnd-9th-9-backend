package com.dnd.Exercise.domain.userField.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BattleStatusDto {

    private String name;

    private Integer totalRecordCount;

    private Integer goalAchievedCount;

    private Integer totalBurnedCalorie;

    private Integer totalExerciseTimeMinute;

    public static BattleStatusDto from(String name, List<Integer> score){
        return BattleStatusDto.builder()
                .name(name)
                .totalRecordCount(score.get(0))
                .goalAchievedCount(score.get(1))
                .totalBurnedCalorie(score.get(2))
                .totalExerciseTimeMinute(score.get(3))
                .build();
    }
}
