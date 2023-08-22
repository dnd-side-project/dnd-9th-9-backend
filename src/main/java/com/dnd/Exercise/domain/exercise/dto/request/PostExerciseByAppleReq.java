package com.dnd.Exercise.domain.exercise.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostExerciseByAppleReq {
    List<AppleWorkoutDto> appleWorkouts;
}
