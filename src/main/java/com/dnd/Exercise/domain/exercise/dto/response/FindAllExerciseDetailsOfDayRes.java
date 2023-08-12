package com.dnd.Exercise.domain.exercise.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindAllExerciseDetailsOfDayRes {

    private List<ExerciseDetailDto> exerciseList;

    private int totalCount;
}
