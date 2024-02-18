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

    private int totalScore;

    private Integer totalRecordCount;

    private Integer goalAchievedCount;

    private Integer totalBurnedCalorie;

    private Integer totalExerciseTimeMinute;

    public static FindFieldResultDto from(Field field, List<Integer> score){
        return FindFieldResultDto.builder()
                .name(field.getName())
                .profileImg(field.getProfileImg())
                .totalScore(0)
                .totalRecordCount(score.get(0))
                .goalAchievedCount(score.get(1))
                .totalBurnedCalorie(score.get(2))
                .totalExerciseTimeMinute(score.get(3))
                .build();
    }

    public void addTotalScore(int score){
        this.totalScore += score;
    }
}
