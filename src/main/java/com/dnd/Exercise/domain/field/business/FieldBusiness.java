package com.dnd.Exercise.domain.field.business;

import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.IN_PROGRESS;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.RECRUITING;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.DUEL;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.TEAM;
import static com.dnd.Exercise.global.common.Constants.REDIS_AUTO_PREFIX;
import static com.dnd.Exercise.global.common.Constants.REDIS_AUTO_SPLIT_REGEX;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_FULL;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_IN_PROGRESS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.DUEL_MAX_ONE;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FIELD_NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.HAVING_IN_PROGRESS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_LEADER;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_MEMBER;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NO_SIMILAR_FIELD_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.RECRUITING_YET;
import static com.dnd.Exercise.global.error.dto.ErrorCode.SHOULD_CREATE;

import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.field.dto.response.ElementWiseWin;
import com.dnd.Exercise.domain.field.dto.response.FindFieldResultDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldResultRes;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldStatus;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.common.RedisService;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.s3.AwsS3Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FieldBusiness {
    private final UserFieldRepository userFieldRepository;
    private final ActivityRingRepository activityRingRepository;
    private final ExerciseRepository exerciseRepository;
    private final FieldRepository fieldRepository;
    private final AwsS3Service awsS3Service;
    private final RedisService redisService;

    public List<Long> getMemberIds(Long fieldId) {
        List<UserField> allMembers = userFieldRepository.findAllByFieldId(fieldId);
        return allMembers.stream().map(userField -> userField.getUser().getId()).collect(Collectors.toList());
    }

    public List<Long> getMemberIds(List<Long> fieldIds) {
        List<UserField> allMembers = userFieldRepository.findAllByFieldIdIn(fieldIds);
        return allMembers.stream().map(userField -> userField.getUser().getId()).collect(Collectors.toList());
    }

    public List<User> getMembers(Long fieldId){
        List<UserField> allMembers = userFieldRepository.findAllByFieldId(fieldId);
        return allMembers.stream().map(UserField::getUser).collect(Collectors.toList());
    }

    public List<Integer> calculateRecord(List<ActivityRing> activityRings, List<Exercise> exercises) {
        int totalRecordCount = exercises.size();
        int goalAchievementCount = getGoalAchievementCount(activityRings);
        int totalBurnedCalorie = getTotalBurnedCalorie(activityRings);
        int totalExerciseTimeMinute = getTotalExerciseTimeMinute(exercises);

        return List.of(totalRecordCount, goalAchievementCount,
                totalBurnedCalorie, totalExerciseTimeMinute);
    }

    private int getTotalExerciseTimeMinute(List<Exercise> exercises) {
        return exercises.stream()
                .mapToInt(Exercise::getDurationMinute)
                .sum();
    }

    private int getTotalBurnedCalorie(List<ActivityRing> activityRings) {
        return activityRings.stream()
                .mapToInt(ActivityRing::getBurnedCalorie)
                .sum();
    }

    private int getGoalAchievementCount(List<ActivityRing> activityRings) {
        return (int) activityRings.stream()
                .filter(ActivityRing::getIsGoalAchieved)
                .count();
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

    public void validateIsNotFull(Field field) {
        if(field.getCurrentSize() == field.getMaxSize()){
            throw new BusinessException(ALREADY_FULL);
        }
    }

    public List<Integer> getFieldSummary(Long fieldId, LocalDate targetDate) {
        List<Long> memberIds = getMemberIds(fieldId);
        List<ActivityRing> activityRings = getActivityRings(targetDate, memberIds);
        List<Exercise> exercises = getExercises(targetDate, memberIds);

        return calculateRecord(activityRings, exercises);
    }

    public List<Integer> getFieldSummary(Long fieldId, LocalDate startDate, LocalDate endDate) {
        List<Long> memberIds = getMemberIds(fieldId);
        List<ActivityRing> activityRings = getActivityRings(startDate, endDate, memberIds);
        List<Exercise> exercises = getExercises(startDate, endDate, memberIds);

        return calculateRecord(activityRings, exercises);
    }

    public List<ActivityRing> getActivityRings(LocalDate date, List<Long> memberIds) {
        return activityRingRepository.findAllByDateAndUserIdIn(date, memberIds);
    }

    public List<ActivityRing> getActivityRings(LocalDate startDate, LocalDate endDate, List<Long> memberIds) {
        return activityRingRepository.findAllByDateBetweenAndUserIdIn(startDate, endDate, memberIds);
    }

    public List<Exercise> getExercises(LocalDate date, List<Long> memberIds) {
        return exerciseRepository.findAllByExerciseDateAndUserIdIn(date, memberIds);
    }

    public List<Exercise> getExercises(LocalDate startDate, LocalDate endDate, List<Long> memberIds) {
        return exerciseRepository.findAllByExerciseDateBetweenAndUserIdIn(
                startDate, endDate, memberIds);
    }

    public WinStatus getFieldWinStatus(Field field) {
        if (TEAM.equals(field.getFieldType())) return null;
        Field opponent = field.getOpponent();
        LocalDate startDate = field.getStartDate();
        LocalDate endDate = field.getEndDate();

        List<Integer> mySummary = getFieldSummary(field.getId(), startDate, endDate);
        List<Integer> opponentSummary = getFieldSummary(opponent.getId(), startDate, endDate);

        return compareSummaries(mySummary, opponentSummary);
    }

    public WinStatus compareSummaries(List<Integer> mySummary, List<Integer> opponentSummary) {
        int result = IntStream.range(0, 4)
                .map(i -> Integer.compare(mySummary.get(i), opponentSummary.get(i)))
                .sum();

        if (result > 0) return WinStatus.WIN;
        if (result < 0) return WinStatus.LOSE;
        return WinStatus.DRAW;
    }

    public void validateNotHavingField(User user, FieldType fieldType){
        if (!userFieldRepository.findByUserAndStatusInAndType(
                user, List.of(RECRUITING, IN_PROGRESS), fieldType).isEmpty()){
            throw new BusinessException(HAVING_IN_PROGRESS);
        }
    }

    public UserField validateHavingField(User user, FieldType fieldType){
        List<UserField> userField = userFieldRepository.findByUserAndStatusInAndType(
                user, List.of(RECRUITING, IN_PROGRESS), fieldType);
        if (userField.isEmpty()) throw new BusinessException(SHOULD_CREATE);
        return userField.get(0);
    }

    public Boolean isFieldInProgress(Field field) {
        return field.getFieldStatus() == FieldStatus.IN_PROGRESS ||
                (field.getFieldStatus() == FieldStatus.RECRUITING && field.getOpponent() != null);
    }

    public void validateDuelMaxSize(FieldType fieldType, int maxSize) {
        if (DUEL.equals(fieldType) && maxSize != 1) {
            throw new BusinessException(DUEL_MAX_ONE);
        }
    }

    public void updateIfNotTeam(Field myField, List<Integer> mySummary, FindFieldResultDto home, FindFieldResultRes result) {
        if (!TEAM.equals(myField.getFieldType())){
            Field opponentField = myField.getOpponent();

            List<Integer> opponentSummary = getPeriodSummary(opponentField);
            FindFieldResultDto away = FindFieldResultDto.from(opponentField, opponentSummary);

            List<WinStatus> elementWiseWinStatus = getElementWiseWinStatus(mySummary, opponentSummary);
            elementWiseWinStatus.forEach(winStatus -> addTotalScore(winStatus, home, away));

            ElementWiseWin elementWiseWin = ElementWiseWin.from(elementWiseWinStatus);
            WinStatus winStatus = compareScore(home.getTotalScore(), away.getTotalScore());

            result.updateIfNotTeam(away, elementWiseWin, winStatus);
        }
    }

    private List<Integer> getPeriodSummary(Field field) {
        Long opponentFieldId = field.getId();
        LocalDate startDate = field.getStartDate();
        LocalDate endDate = field.getEndDate();

        List<Integer> summary = getFieldSummary(opponentFieldId, startDate, endDate);
        return summary;
    }

    private void addTotalScore(WinStatus winStatus, FindFieldResultDto home, FindFieldResultDto away) {
        if (winStatus == WinStatus.WIN)
            home.addTotalScore(1);
        else if (winStatus == WinStatus.LOSE)
            away.addTotalScore(1);
    }

    private List<WinStatus> getElementWiseWinStatus(List<Integer> myScores, List<Integer> opponentScores){
        List<WinStatus> winStatusList = new ArrayList<>();
        for (int idx = 0; idx < myScores.size(); idx++){
            WinStatus winStatus = compareScore(myScores.get(idx), opponentScores.get(idx));
            winStatusList.add(winStatus);
        }
        return winStatusList;
    }

    private WinStatus compareScore(Double myScore, Double opponentScore) {
        if (myScore > opponentScore) return WinStatus.WIN;
        else if (myScore < opponentScore) return WinStatus.LOSE;
        else return WinStatus.DRAW;
    }

    private WinStatus compareScore(Integer myScore, Integer opponentScore) {
        return compareScore(myScore.doubleValue(), opponentScore.doubleValue());
    }

    public void deleteProfileImgIfPresent(Field field) {
        if(field.getProfileImg() != null)
            awsS3Service.deleteImage(field.getProfileImg());
    }

    public Field findBestMatchingField(Field myField, List<Field> allFittingFields, List<String> matchedIds) {
        return allFittingFields.stream()
                .filter(field -> myField.isNotSameField(field) && field.isNotMatchedField(matchedIds))
                .min(Comparator.comparingInt(myField::calculateFieldDifference))
                .orElseThrow(() -> handleNoMatchingFieldFound(myField));
    }

    public List<String> getMatchedIds(String redisValue) {
        List<String> matchedIds = new ArrayList<>();
        if (redisValue != null){
            List<String> ids = List.of(redisValue.split(REDIS_AUTO_SPLIT_REGEX));
            matchedIds.addAll(ids);
        }
        return matchedIds;
    }

    public List<Long> getTargetUserIds(Field field) {
        List<Long> memberIds;
        Long myFieldId = field.getId();
        if (field.getFieldType() == DUEL){
            Long opponentFieldId = field.getOpponent().getId();
            memberIds = getMemberIds(List.of(opponentFieldId, myFieldId));
        }
        else memberIds = getMemberIds(myFieldId);
        return memberIds;
    }

    private BusinessException handleNoMatchingFieldFound(Field myField) {
        redisService.deleteValues(REDIS_AUTO_PREFIX + myField.getId());
        return new BusinessException(NO_SIMILAR_FIELD_FOUND);
    }


}
