package com.dnd.Exercise.domain.userField.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccumulatedExerciseDto {
    private String name;
    private Long totalDurationTime;
    private Long exerciseCount;
}
