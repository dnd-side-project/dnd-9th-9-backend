package com.dnd.Exercise.domain.exercise.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostExerciseByAppleReq {
    List<AppleWorkoutDto> appleWorkouts;
}
