package com.dnd.Exercise.domain.userField.dto;

import lombok.Data;

@Data
public class AccumulatedExerciseDto {
    private String name;
    private int totalDurationTime;
    private int exerciseCount;
}
