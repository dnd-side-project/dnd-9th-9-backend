package com.dnd.Exercise.global.util.field;

import static com.dnd.Exercise.domain.field.entity.FieldStatus.IN_PROGRESS;
import static com.dnd.Exercise.domain.field.entity.FieldStatus.RECRUITING;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_IN_PROGRESS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FIELD_NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.HAVING_IN_PROGRESS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_LEADER;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_MEMBER;
import static com.dnd.Exercise.global.error.dto.ErrorCode.RECRUITING_YET;
import static com.dnd.Exercise.global.error.dto.ErrorCode.SHOULD_CREATE;

import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FieldUtil {

    private final UserFieldRepository userFieldRepository;
    private final ActivityRingRepository activityRingRepository;
    private final ExerciseRepository exerciseRepository;
    private final FieldRepository fieldRepository;

    public List<Long> getMemberIds(Long fieldId) {
        List<UserField> allMembers = userFieldRepository.findAllByField(fieldId);
        return allMembers.stream().map(userField -> userField.getUser().getId()).collect(Collectors.toList());
    }

    public List<ActivityRing> getActivityRings(LocalDate date, List<Long> memberIds) {
        return activityRingRepository.findAllByDateAndUserIdIn(date, memberIds);
    }

    public List<Exercise> getExercises(LocalDate date, List<Long> memberIds) {
        return exerciseRepository.findAllByExerciseDateAndUserIdIn(date, memberIds);
    }

    public List<ActivityRing> getActivityRings(LocalDate startDate, LocalDate endDate, List<Long> memberIds) {
        return activityRingRepository.findAllByDateBetweenAndUserIdIn(
                startDate, endDate, memberIds);
    }

    public List<Exercise> getExercises(LocalDate startDate, LocalDate endDate, List<Long> memberIds) {
        return exerciseRepository.findAllByExerciseDateBetweenAndUserIdIn(
                startDate, endDate, memberIds);
    }

    public List<Integer> calculateScore(List<ActivityRing> activityRings, List<Exercise> exercises) {
        int totalRecordCount = exercises.size();
        int goalAchievementCount = (int) activityRings.stream().filter(ActivityRing::getIsGoalAchieved).count();
        int totalBurnedCalorie = activityRings.stream().mapToInt(ActivityRing::getBurnedCalorie).sum();
        int totalExerciseTimeMinute = exercises.stream().mapToInt(Exercise::getDurationMinute).sum();

        return List.of(totalRecordCount, goalAchievementCount, totalBurnedCalorie, totalExerciseTimeMinute);
    }

    public Field getField(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new BusinessException(FIELD_NOT_FOUND));
    }

    public void validateIsLeader(Long userId, Long leaderId) {
        if(!userId.equals(leaderId)){
            throw new BusinessException(NOT_LEADER);
        }
    }

    public void validateIsMember(User user, Field field) {
        if (!userFieldRepository.existsByFieldAndUser(field, user)) {
            throw new BusinessException(NOT_MEMBER);
        }
    }

    public void validateHaveOpponent(Field field) {
        if(field.getOpponent() != null){
            throw new BusinessException(ALREADY_IN_PROGRESS);
        }
    }

    public void validateIsFull(Field field) {
        if(field.getCurrentSize() != field.getMaxSize()){
            throw new BusinessException(RECRUITING_YET);
        }
    }

    public List<Integer> getFieldSummary(Long fieldId, LocalDate targetDate) {
        List<Long> memberIds = getMemberIds(fieldId);
        List<ActivityRing> activityRings = getActivityRings(targetDate, memberIds);
        List<Exercise> exercises = getExercises(targetDate, memberIds);

        return calculateScore(activityRings, exercises);
    }

    public List<Integer> getFieldSummary(Long fieldId, LocalDate startDate, LocalDate endDate) {
        List<Long> memberIds = getMemberIds(fieldId);
        List<ActivityRing> activityRings = getActivityRings(startDate, endDate, memberIds);
        List<Exercise> exercises = getExercises(startDate, endDate, memberIds);

        return calculateScore(activityRings, exercises);
    }

    public void validateNotHavingField(User user, FieldType fieldType){
        if (!userFieldRepository.findByUserAndStatusInAndType(
                user, List.of(RECRUITING, IN_PROGRESS), List.of(fieldType))
                .isEmpty()){
            throw new BusinessException(HAVING_IN_PROGRESS);
        }
    }

    public UserField validateHavingField(User user, FieldType fieldType){
        List<UserField> userField = userFieldRepository.findByUserAndStatusInAndType(
                user, List.of(RECRUITING, IN_PROGRESS), List.of(fieldType));
        if (userField.isEmpty()){
            throw new BusinessException(SHOULD_CREATE);
        }
        return userField.get(0);
    }

}
