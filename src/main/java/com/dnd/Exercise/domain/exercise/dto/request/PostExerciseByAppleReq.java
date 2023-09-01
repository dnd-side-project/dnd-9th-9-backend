package com.dnd.Exercise.domain.exercise.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostExerciseByAppleReq {
    List<@Valid AppleWorkoutDto> appleWorkouts;
}
