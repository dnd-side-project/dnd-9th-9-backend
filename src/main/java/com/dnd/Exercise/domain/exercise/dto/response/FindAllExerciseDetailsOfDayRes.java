package com.dnd.Exercise.domain.exercise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllExerciseDetailsOfDayRes {

    private List<ExerciseDetailDto> exerciseList;

    private int totalCount;
}
