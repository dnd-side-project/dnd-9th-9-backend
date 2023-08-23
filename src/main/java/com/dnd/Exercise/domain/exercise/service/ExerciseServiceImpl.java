package com.dnd.Exercise.domain.exercise.service;

import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.exercise.dto.ExerciseMapper;
import com.dnd.Exercise.domain.exercise.dto.request.AppleWorkoutDto;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByAppleReq;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.ExerciseDetailDto;
import com.dnd.Exercise.domain.exercise.dto.response.FindAllExerciseDetailsOfDayRes;
import com.dnd.Exercise.domain.exercise.dto.response.GetCalorieStateRes;
import com.dnd.Exercise.domain.exercise.dto.response.GetMyExerciseSummaryRes;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseServiceImpl implements ExerciseService{

    private final ExerciseRepository exerciseRepository;
    private final ActivityRingRepository activityRingRepository;

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

    @Override
    @Transactional
    public void postExerciseByApple(PostExerciseByAppleReq postExerciseByAppleReq, User user) {
        List<AppleWorkoutDto> appleWorkouts = postExerciseByAppleReq.getAppleWorkouts();

        List<Exercise> newWorkOuts = new ArrayList<>();
        appleWorkouts.forEach(workout -> {
            Exercise exercise = exerciseRepository.findByAppleUidAndUserId(workout.getAppleUid(), user.getId()).orElse(null);
            if (exercise != null) {
                exercise.updateAppleWorkout(workout);
            } else {
                newWorkOuts.add(workout.toEntityWithUser(user));
            }
        });
        exerciseRepository.saveAll(newWorkOuts);

        List<String> existingAppleUids = appleWorkouts.stream()
                .map(workout -> workout.getAppleUid())
                .collect(Collectors.toList());
        exerciseRepository.deleteAppleWorkouts(existingAppleUids);
    }

    @Override
    public GetCalorieStateRes getCalorieState(LocalDate date, User user) {
        ActivityRing activityRing= activityRingRepository.findByDateAndUserId(date,user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ACTIVITY_RING_NOT_FOUND));

        return GetCalorieStateRes.builder()
                .goalCalorie(user.getCalorieGoal())
                .burnedCalorie(activityRing.getBurnedCalorie())
                .build();
    }

    @Override
    public GetMyExerciseSummaryRes getMyExerciseSummary(LocalDate date, User user) {
        int totalBurnedCalorie = 0;
        ActivityRing activityRing = activityRingRepository.findByDateAndUserId(date,user.getId()).orElse(null);
        if (activityRing != null) {
            totalBurnedCalorie = activityRing.getBurnedCalorie();
        }

        int totalExerciseCalorie = exerciseRepository.sumDailyBurnedCalorieOfUser(date,user.getId());
        int totalExerciseTimeMinute = exerciseRepository.sumDailyDurationMinuteOfUser(date,user.getId());
        int totalRecordCount = exerciseRepository.countByExerciseDateAndUserId(date,user.getId());

        return GetMyExerciseSummaryRes.builder()
                .totalBurnedCalorie(totalBurnedCalorie)
                .totalExerciseCalorie(totalExerciseCalorie)
                .totalExerciseTimeMinute(totalExerciseTimeMinute)
                .totalRecordCount(totalRecordCount)
                .build();
    }
}
