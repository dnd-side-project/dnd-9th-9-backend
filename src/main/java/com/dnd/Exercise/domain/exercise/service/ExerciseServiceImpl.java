package com.dnd.Exercise.domain.exercise.service;

import com.dnd.Exercise.domain.exercise.dto.ExerciseMapper;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.ExerciseDetailDto;
import com.dnd.Exercise.domain.exercise.dto.response.FindAllExerciseDetailsOfDayRes;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseServiceImpl implements ExerciseService{

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Override
    public FindAllExerciseDetailsOfDayRes findAllExerciseDetailsOfDay(LocalDate date, Long userId) {
        List<ExerciseDetailDto> exercises = exerciseRepository.findAllExercisesByDateAndUser(date,userId)
                .stream()
                .map(exerciseMapper::from)
                .collect(Collectors.toList());

        return FindAllExerciseDetailsOfDayRes.builder()
                .exerciseList(exercises)
                .totalCount(exercises.size())
                .build();
    }

    @Override
    @Transactional
    public void postExerciseByCommon(PostExerciseByCommonReq postExerciseByCommonReq, User user) {
        Exercise exercise = postExerciseByCommonReq.toEntityWithUser(user);
        exerciseRepository.save(exercise);
    }

    @Override
    @Transactional
    public void updateExercise(long exerciseId, UpdateExerciseReq updateExerciseReq) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        exerciseMapper.updateFromDto(updateExerciseReq,exercise);
    }

    @Override
    @Transactional
    public void deleteExercise(long exerciseId) {
        exerciseRepository.delete(
                exerciseRepository.findById(exerciseId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND))
        );
    }
}
