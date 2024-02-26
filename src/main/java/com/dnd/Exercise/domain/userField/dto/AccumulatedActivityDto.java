package com.dnd.Exercise.domain.userField.dto;

import lombok.Data;

@Data
public class AccumulatedActivityDto {
    private String name;
    private int totalBurnedCalorie;
    private int goalAchievedCount;
}
