package com.dnd.Exercise.domain.exercise.service;

import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.FindAllExerciseDetailsOfDayRes;
import com.dnd.Exercise.domain.user.entity.User;

import java.time.LocalDate;

public interface ExerciseService {
    FindAllExerciseDetailsOfDayRes findAllExerciseDetailsOfDay(LocalDate date, Long userId);
    void postExerciseByCommon(PostExerciseByCommonReq postExerciseByCommonReq, User user);
    void updateExercise(long exerciseId, UpdateExerciseReq updateExerciseReq);
    void deleteExercise(long exerciseId);
}
