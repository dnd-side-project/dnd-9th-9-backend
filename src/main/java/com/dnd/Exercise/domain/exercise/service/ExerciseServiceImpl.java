package com.dnd.Exercise.domain.exercise.service;

import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.exercise.dto.ExerciseMapper;
import com.dnd.Exercise.domain.exercise.dto.request.AppleWorkoutDto;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByAppleReq;
import com.dnd.Exercise.domain.exercise.dto.request.PostExerciseByCommonReq;
import com.dnd.Exercise.domain.exercise.dto.request.UpdateExerciseReq;
import com.dnd.Exercise.domain.exercise.dto.response.*;
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
        int totalBurnedCalorie = getTotalBurnedCalorie(date,user.getId());
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

    @Override
    public GetRecentsRes getRecent(LocalDate date, User user) {
        int totalExerciseMinute = exerciseRepository.sumDailyDurationMinuteOfUser(date,user.getId());
        int totalBurnedCalorie = getTotalBurnedCalorie(date, user.getId());
        List<RecentSportsDto> recentSports = exerciseRepository.getDailyRecentSports(date,user.getId());

        return GetRecentsRes.builder()
                .totalExerciseMinute(totalExerciseMinute)
                .totalBurnedCalorie(totalBurnedCalorie)
                .recentSports(recentSports)
                .build();
    }

    public int getTotalBurnedCalorie(LocalDate date, long userId) {
        // 현재 유저의 '특정 하루에 대한 총 소비 칼로리'는 애플 활동링 칼로리를 기준으로 함. 연동 유저 + 워치 소유자라고 가정.
        // -> TODO: 애플 연동/비연동 유저 구분 필요. 연동 유저인 경우 활동링 칼로리, 비연동 유저인 경우 exercise 테이블 합?
        // -> TODO: 워치 소유자가 아닌 연동 유저는...? 활동링 칼로리값 + exercise 테이블 합으로 나타나야 하는게 맞는데, 서비스 상에서 워치 소유자 구분 불가.

        int totalBurnedCalorie = 0;
        ActivityRing activityRing = activityRingRepository.findByDateAndUserId(date,userId).orElse(null);
        if (activityRing != null) {
            totalBurnedCalorie = activityRing.getBurnedCalorie();
        }
        return totalBurnedCalorie;
    }
}
