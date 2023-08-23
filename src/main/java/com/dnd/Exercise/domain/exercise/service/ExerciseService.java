package com.dnd.Exercise.domain.exercise.service;

import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByAppleReq;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.FindAllExerciseDetailsOfDayRes;
import com.dnd.Exercise.domain.exercise.dto.response.GetCalorieStateRes;
import com.dnd.Exercise.domain.exercise.dto.response.GetMyExerciseSummaryRes;
import com.dnd.Exercise.domain.exercise.dto.response.GetRecentsRes;
import com.dnd.Exercise.domain.user.entity.User;

import java.time.LocalDate;

public interface ExerciseService {
    FindAllExerciseDetailsOfDayRes findAllExerciseDetailsOfDay(LocalDate date, Long userId);
    void postExerciseByCommon(PostExerciseByCommonReq postExerciseByCommonReq, User user);
    void updateExercise(long exerciseId, UpdateExerciseReq updateExerciseReq);
    void deleteExercise(long exerciseId);
    void postExerciseByApple(PostExerciseByAppleReq postExerciseByAppleReq, User user);
    GetCalorieStateRes getCalorieState(LocalDate date, User user);
    GetMyExerciseSummaryRes getMyExerciseSummary(LocalDate date, User user);
    GetRecentsRes getRecent(LocalDate date, User user);
}
