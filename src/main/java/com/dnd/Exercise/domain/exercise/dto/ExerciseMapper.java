package com.dnd.Exercise.domain.exercise.dto;

import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.ExerciseDetailDto;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    ExerciseDetailDto from(Exercise exercise);

    Exercise from(PostExerciseByCommonReq postExerciseByCommonReq);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE,
            unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateFromDto(UpdateExerciseReq updateExerciseReq, @MappingTarget Exercise exercise);
}
