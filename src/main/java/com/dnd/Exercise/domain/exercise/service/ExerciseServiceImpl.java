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
import com.dnd.Exercise.global.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    private final AwsS3Service awsS3Service;
    private final String S3_FOLDER = "exercise-memo";

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
        String imgUrl = postMemoImgAtS3(postExerciseByCommonReq.getMemoImgFile());
        Exercise exercise = postExerciseByCommonReq.toEntityWithUserAndMemoImg(user,imgUrl);
        exerciseRepository.save(exercise);
    }

    @Override
    @Transactional
    public void updateExercise(long exerciseId, UpdateExerciseReq updateExerciseReq) {
        Exercise exercise = getExercise(exerciseId);

        if (updateExerciseReq.getDeletePrevImg() == true) {
            deleteMemoImgAtS3(exercise.getMemoImg());
            exercise.updateMemoImgUrl(null);
        }

        if (updateExerciseReq.getNewMemoImgFile() != null) {
            String newImgUrl = postMemoImgAtS3(updateExerciseReq.getNewMemoImgFile());
            exercise.updateMemoImgUrl(newImgUrl);
        }

        exerciseMapper.updateFromDto(updateExerciseReq,exercise);
    }

    @Override
    @Transactional
    public void deleteExercise(long exerciseId) {
        Exercise exercise = getExercise(exerciseId);
        deleteMemoImgAtS3(exercise.getMemoImg());
        exerciseRepository.delete(exercise);
    }

    @Override
    @Transactional
    public void postExerciseByApple(PostExerciseByAppleReq postExerciseByAppleReq, User user) {
        validateIsAppleLinked(user);
        List<AppleWorkoutDto> appleWorkouts = postExerciseByAppleReq.getAppleWorkouts();
        syncAppleWorkouts(appleWorkouts,user);
    }

    @Override
    public GetCalorieStateRes getCalorieState(LocalDate date, User user) {
        int burnedCalorie = getDailyTotalBurnedCalorie(date,user);

        return GetCalorieStateRes.builder()
                .goalCalorie(user.getCalorieGoal())
                .burnedCalorie(burnedCalorie)
                .build();
    }

    @Override
    public GetMyExerciseSummaryRes getMyExerciseSummary(LocalDate date, User user) {
        // TODO: 연동유저인 경우 '오늘 하루 총 소비 칼로리' 를 '활동링 칼로리' 로 취급하기 때문에, <연동유저이지만 워치가 없는 경우> '총 소비 칼로리 < 총 운동 칼로리' 일수도 있을것 같다.
        // TODO: -> '총 소비 칼로리 < 총 운동 칼로리' 인 경우 워치가 없는 유저로 판단하고 '활동링 칼로리 + 운동 칼로리' 합산? 둘 사이에 겹치는 칼로리가 있진 않을지 고려해봐야 할 듯
        int totalBurnedCalorie = getDailyTotalBurnedCalorie(date,user);
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
        int totalBurnedCalorie = getDailyTotalBurnedCalorie(date, user);
        List<RecentSportsDto> recentSports = exerciseRepository.findDailyRecentSports(date,user.getId());

        return GetRecentsRes.builder()
                .totalExerciseMinute(totalExerciseMinute)
                .totalBurnedCalorie(totalBurnedCalorie)
                .recentSports(recentSports)
                .build();
    }

    private int getDailyTotalBurnedCalorie(LocalDate date, User user) {
        int totalBurnedCalorie = 0;

        if (user.getIsAppleLinked() == true) {
            ActivityRing activityRing = activityRingRepository.findByDateAndUserId(date,user.getId()).orElse(null);
            if (activityRing != null) {
                totalBurnedCalorie = activityRing.getBurnedCalorie();
            }
        } else {
            totalBurnedCalorie = exerciseRepository.sumDailyBurnedCalorieOfUser(date,user.getId());
        }

        return totalBurnedCalorie;
    }

    private Exercise getExercise (long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        return exercise;
    }

    private String postMemoImgAtS3(MultipartFile memoImg) {
        String imgUrl = null;
        if (memoImg != null) {
            imgUrl = awsS3Service.upload(memoImg,S3_FOLDER);
        }
        return imgUrl;
    }

    private void deleteMemoImgAtS3(String fileName) {
        if(fileName != null) {
            awsS3Service.deleteImage(fileName);
        }
    }

    private void syncAppleWorkouts(List<AppleWorkoutDto> appleWorkouts, User user) {
        saveOrUpdateWorkouts(appleWorkouts,user);
        deleteFadedWorkouts(appleWorkouts);
    }

    private void saveOrUpdateWorkouts(List<AppleWorkoutDto> appleWorkouts, User user) {
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
    }

    private void deleteFadedWorkouts(List<AppleWorkoutDto> appleWorkouts) {
        List<String> existingAppleUids = appleWorkouts.stream()
                .map(AppleWorkoutDto::getAppleUid)
                .collect(Collectors.toList());
        exerciseRepository.deleteUnexistingAppleWorkouts(existingAppleUids);
    }

    private void validateIsAppleLinked(User user) {
        if (!user.getIsAppleLinked()) {
            throw new BusinessException(ErrorCode.APPLE_WORKOUTS_UPDATE_UNAVAILABLE);
        }
    }
}
