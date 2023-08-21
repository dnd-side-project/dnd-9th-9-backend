package com.dnd.Exercise.domain.userField.service;

import static com.dnd.Exercise.domain.field.entity.FieldStatus.*;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_FOUND;

import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.dto.UserFieldMapper;
import com.dnd.Exercise.domain.userField.dto.response.BattleStatusDto;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserFieldServiceImpl implements UserFieldService {

    private final UserFieldRepository userFieldRepository;
    private final FieldRepository fieldRepository;
    private final UserFieldMapper userFieldMapper;
    private final ExerciseRepository exerciseRepository;
    private final ActivityRingRepository activityRingRepository;

    @Override
    public List<FindAllMembersRes> findAllMembers(Long fieldId) {
        Field field = getField(fieldId);
        Long leaderId = field.getLeaderId();
        List<UserField> allMembers = userFieldRepository.findAllByField(fieldId);

        return allMembers.stream().map(member -> {
                    FindAllMembersRes findAllMembersRes =
                            userFieldMapper.toFindAllMembersRes(member, member.getUser());
                    findAllMembersRes.setIsLeader(leaderId.equals(findAllMembersRes.getId()));
                    return findAllMembersRes;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FindAllFieldsDto> findAllMyInProgressFields(User user) {
        List<UserField> myUserFields = userFieldRepository.findByUserAndStatusIn(user,
                List.of(RECRUITING, IN_PROGRESS));

        return myUserFields.stream()
                .filter(userField -> {
                    Field field = userField.getField();
                    return !(RECRUITING.equals(field.getFieldStatus()) && field.getOpponent() == null);
                })
                .map(userField -> userFieldMapper.toFindAllFieldsDto(userField.getField()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FindAllFieldsDto> findAllMyCompletedFields(User user, FieldType fieldType,
            Pageable pageable) {
        List<UserField> myUserFields = userFieldRepository.findCompletedFieldByUserAndType(
                user, fieldType, pageable);

        return myUserFields.stream()
                .map(userField -> userFieldMapper.toFindAllFieldsDto(userField.getField()))
                .collect(Collectors.toList());

    }

    @Override
    public FindMyBattleStatusRes findMyBattleStatus(User user) {
        List<UserField> inProgressUserField = userFieldRepository.findByUserAndStatusIn(user,
                List.of(IN_PROGRESS));
        if (inProgressUserField.isEmpty()){
            return null;
        }
        Field myField = inProgressUserField.get(0).getField();
        Field opponentField = myField.getOpponent();

        LocalDate startDate = myField.getStartDate();

        List<Long> memberIds = getMemberIds(myField.getId());
        List<Long> opponentMemberIds = getMemberIds(opponentField.getId());

        List<ActivityRing> activityRings = getActivityRings(startDate, memberIds);
        List<ActivityRing> opponentActivityRings = getActivityRings(startDate, opponentMemberIds);

        List<Exercise> exercises = getExercises(startDate, memberIds);
        List<Exercise> opponentExercises = getExercises(startDate, opponentMemberIds);

        List<Integer> status = calculateSummary(activityRings, exercises);
        List<Integer> opponentStatus = calculateSummary(opponentActivityRings, opponentExercises);

        BattleStatusDto home = initStatus(myField.getName(), status);
        BattleStatusDto away = initStatus(opponentField.getName(), opponentStatus);

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), myField.getEndDate());

        return FindMyBattleStatusRes.builder()
                .fieldId(myField.getId())
                .daysLeft(daysLeft)
                .home(home)
                .away(away)
                .build();
    }

    private BattleStatusDto initStatus(String name, List<Integer> status) {
        return new BattleStatusDto(
                name,
                status.get(0),
                status.get(1),
                status.get(3),
                status.get(2));
    }

    private List<Integer> calculateSummary(List<ActivityRing> activityRings, List<Exercise> exercises) {
        int totalRecordCount = exercises.size();
        int goalAchievementCount = (int) activityRings.stream().filter(ActivityRing::getIsGoalAchieved).count();
        int totalExerciseTimeMinute = exercises.stream().mapToInt(Exercise::getDurationMinute).sum();
        int totalBurnedCalorie = activityRings.stream().mapToInt(ActivityRing::getBurnedCalorie).sum();

        return List.of(totalRecordCount, goalAchievementCount, totalExerciseTimeMinute, totalBurnedCalorie);
    }

    private List<ActivityRing> getActivityRings(LocalDate startDate, List<Long> memberIds) {
        return activityRingRepository.findAllByDateBetweenAndUserIdIn(startDate, LocalDate.now(), memberIds);
    }

    private List<Exercise> getExercises(LocalDate startDate, List<Long> memberIds) {
        return exerciseRepository.findAllByExerciseDateBetweenAndUserIdIn(startDate, LocalDate.now(), memberIds);
    }

    private List<Long> getMemberIds(Long fieldId) {
        List<UserField> allMembers = userFieldRepository.findAllByField(fieldId);
        return allMembers.stream().map(userField -> userField.getUser().getId()).collect(Collectors.toList());
    }

    private Field getField(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
    }
}
