package com.dnd.Exercise.domain.exercise.dto.response;

import com.dnd.Exercise.domain.sports.entity.Sports;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecentSportsDto {
    Sports sports;

    int exerciseMinute;

    int burnedCalorie;
}
