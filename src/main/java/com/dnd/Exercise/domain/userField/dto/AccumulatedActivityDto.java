package com.dnd.Exercise.domain.userField.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccumulatedActivityDto {
    private String name;
    private Long totalBurnedCalorie;
    private Long goalAchievedCount;
}
