package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.Field;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindFieldResultDto {
    private String name;

    private String profileImg;

    private Integer totalRecordCount;

    private Integer goalAchievedCount;

    private Integer totalBurnedCalorie;

    private Integer totalExerciseTimeMinute;

    public FindFieldResultDto(Field field, List<Integer> score){
        this.name = field.getName();
        this.profileImg = field.getProfileImg();
        this.totalRecordCount = score.get(0);
        this.goalAchievedCount = score.get(1);
        this.totalBurnedCalorie = score.get(2);
        this.totalExerciseTimeMinute = score.get(3);
    }
}
