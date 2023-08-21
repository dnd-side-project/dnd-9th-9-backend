package com.dnd.Exercise.domain.exercise.dto;

import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.ExerciseDetailDto;
import com.dnd.Exercise.domain.exercise.dto.response.FindAllExerciseDetailsOfDayRes;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.global.util.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExerciseMapper extends GenericMapper<UpdateExerciseReq, Exercise> {
    ExerciseDetailDto from(Exercise exercise);

    Exercise from(PostExerciseByCommonReq postExerciseByCommonReq);
}
