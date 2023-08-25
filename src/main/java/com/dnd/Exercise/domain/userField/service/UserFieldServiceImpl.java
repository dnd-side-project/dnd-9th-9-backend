package com.dnd.Exercise.domain.userField.service;

import static com.dnd.Exercise.domain.field.entity.FieldStatus.*;
import static com.dnd.Exercise.domain.field.entity.FieldType.*;
import static com.dnd.Exercise.domain.field.entity.RankCriterion.BURNED_CALORIE;
import static com.dnd.Exercise.domain.field.entity.RankCriterion.EXERCISE_TIME;
import static com.dnd.Exercise.domain.field.entity.RankCriterion.GOAL_ACHIEVED;
import static com.dnd.Exercise.domain.field.entity.RankCriterion.RECORD_COUNT;

import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.entity.BattleType;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.RankCriterion;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.dto.UserFieldMapper;
import com.dnd.Exercise.domain.userField.dto.response.BattleStatusDto;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyTeamStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.TopPlayerDto;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.util.field.FieldUtil;
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
    private final UserFieldMapper userFieldMapper;
    private final ExerciseRepository exerciseRepository;
    private final ActivityRingRepository activityRingRepository;
    private final FieldUtil fieldUtil;

    private TopPlayerDto getTopUserByCriteria(
            RankCriterion criterion, LocalDate startDate, List<Long> memberIds) {
        if (criterion == BURNED_CALORIE || criterion == GOAL_ACHIEVED) {
            return activityRingRepository.findAccumulatedTopByDynamicCriteria(
                    criterion, startDate, memberIds);
        } else {
            return exerciseRepository.findAccumulatedTopByDynamicCriteria(
                    criterion, startDate, memberIds);
        }
    }

    @Override
    public List<FindAllMembersRes> findAllMembers(Long fieldId) {
        Field field = fieldUtil.getField(fieldId);
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
        List<UserField> myUserFields = userFieldRepository.findByUserAndStatusInAndType(user,
                List.of(RECRUITING, IN_PROGRESS), List.of(TEAM, TEAM_BATTLE, DUEL));

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
    public FindMyBattleStatusRes findMyBattleStatus(User user, BattleType battleType) {
        List<UserField> inProgressUserField = userFieldRepository.findByUserAndStatusInAndType(user,
                List.of(IN_PROGRESS), List.of(battleType.toFieldType()));

        if (inProgressUserField.isEmpty()){
            return null;
        }
        Field myField = inProgressUserField.get(0).getField();
        Field opponentField = myField.getOpponent();

        LocalDate startDate = myField.getStartDate();

        List<Integer> score = fieldUtil.getFieldSummary(myField.getId(), startDate, LocalDate.now());
        List<Integer> opponentScore = fieldUtil.getFieldSummary(opponentField.getId(), startDate, LocalDate.now());

        BattleStatusDto home = new BattleStatusDto(myField.getName(), score);
        BattleStatusDto away = new BattleStatusDto(opponentField.getName(), opponentScore);

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), myField.getEndDate());

        return FindMyBattleStatusRes.builder()
                .fieldId(myField.getId())
                .daysLeft(daysLeft)
                .home(home)
                .away(away)
                .build();
    }

    @Override
    public FindMyTeamStatusRes findMyTeamStatus(User user) {
        List<UserField> inProgressUserField = userFieldRepository.findByUserAndStatusInAndType(user,
                List.of(IN_PROGRESS), List.of(TEAM));

        if (inProgressUserField.isEmpty()){
            return null;
        }
        Field myField = inProgressUserField.get(0).getField();
        List<Long> memberIds = fieldUtil.getMemberIds(myField.getId());

        LocalDate startDate = myField.getStartDate();
        LocalDate endDate = myField.getEndDate();
        String teamName = myField.getName();
        Long fieldId = myField.getId();

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), endDate);

        return FindMyTeamStatusRes.builder()
                .fieldId(fieldId)
                .teamName(teamName)
                .daysLeft(daysLeft)
                .recordCount(getTopUserByCriteria(RECORD_COUNT, startDate, memberIds))
                .exerciseTimeMinute(getTopUserByCriteria(EXERCISE_TIME, startDate, memberIds))
                .burnedCalorie(getTopUserByCriteria(BURNED_CALORIE, startDate, memberIds))
                .goalAchievedCount(getTopUserByCriteria(GOAL_ACHIEVED, startDate, memberIds))
                .build();
    }
}
