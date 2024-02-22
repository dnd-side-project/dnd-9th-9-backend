package com.dnd.Exercise.domain.userField.service;

import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.*;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.*;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.BURNED_CALORIE;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.EXERCISE_TIME;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.GOAL_ACHIEVED;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.RECORD_COUNT;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.ALERT;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.CHEER;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.EJECT;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.EXIT;
import static com.dnd.Exercise.global.common.Constants.REDIS_CHEER_PREFIX;
import static com.dnd.Exercise.global.common.Constants.REDIS_NOTIFICATION_VERIFIED;
import static com.dnd.Exercise.global.common.Constants.REDIS_WAKEUP_PREFIX;
import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FCM_TIME_LIMIT;
import static com.dnd.Exercise.global.error.dto.ErrorCode.MUST_NOT_LEADER;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_MEMBER;
import static com.dnd.Exercise.global.error.dto.ErrorCode.SHOULD_CREATE;

import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.userField.dto.UserFieldMapper;
import com.dnd.Exercise.domain.userField.dto.response.BattleStatusDto;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMyCompletedFieldsRes;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMyFieldsDto;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyTeamStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.TopPlayerDto;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.common.RedisService;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.util.field.FieldUtil;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final UserRepository userRepository;
    private final BattleEntryRepository battleEntryRepository;
    private final FieldUtil fieldUtil;
    private final NotificationService notificationService;
    private final RedisService redisService;
    



    @Override
    public List<FindAllMembersRes> findAllMembers(Long fieldId) {
        Field field = fieldUtil.getField(fieldId);
        Long leaderId = field.getLeaderId();
        List<UserField> allMembers = userFieldRepository.findAllByFieldId(fieldId);

        return allMembers.stream().map(member -> {
                    FindAllMembersRes findAllMembersRes =
                            userFieldMapper.toFindAllMembersRes(member, member.getUser());
                    findAllMembersRes.setIsLeader(leaderId.equals(findAllMembersRes.getId()));
                    return findAllMembersRes;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FindAllMyFieldsDto> findAllMyRecruitingFields(User user) {
        List<UserField> myUserFields = userFieldRepository.findByUserAndStatusIn(user, List.of(RECRUITING));

        return myUserFields.stream()
                .filter(userField -> userField.getField().getOpponent() == null)
                .map(userField -> getFindAllMyFieldsDto(user, userField))
                .collect(Collectors.toList());
    }



    @Override
    public List<FindAllMyFieldsDto> findAllMyInProgressFields(User user) {
        List<UserField> myUserFields = userFieldRepository.findByUserAndStatusInAndTypeIn(user,
                List.of(RECRUITING, IN_PROGRESS), List.of(TEAM, TEAM_BATTLE, DUEL));

        return myUserFields.stream()
                .filter(userField -> userField.getField().getOpponent() != null)
                .map(userField -> getFindAllMyFieldsDto(user, userField))
                .collect(Collectors.toList());
    }

    @Override
    public FindAllMyCompletedFieldsRes findAllMyCompletedFields(User user, FieldType fieldType,
            Pageable pageable) {
        Page<UserField> queryResult = userFieldRepository.findCompletedFieldByUserAndType(
                user, fieldType, pageable);

        List<UserField> myUserFields = queryResult.getContent();
        Long totalCount = queryResult.getTotalElements();

        List<FindAllMyFieldsDto> completedFields = myUserFields.stream()
                .map(userField -> getFindAllMyFieldsDto(user, userField))
                .collect(Collectors.toList());

        return FindAllMyCompletedFieldsRes.builder()
                .completedFields(completedFields)
                .totalCount(totalCount)
                .currentPageSize(pageable.getPageSize())
                .currentPageNumber(pageable.getPageNumber())
                .build();
    }

    @Override
    public FindMyBattleStatusRes findMyBattleStatus(User user, BattleType battleType) {
        List<UserField> inProgressUserField = userFieldRepository.findByUserAndStatusInAndType(user,
                List.of(IN_PROGRESS), battleType.toFieldType());

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
                List.of(IN_PROGRESS), TEAM);

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

    @Transactional
    @Override
    public void ejectMember(User user, Long fieldId, List<Long> ids) {
        Field field = fieldUtil.getField(fieldId);
        fieldUtil.validateIsLeader(user.getId(), field.getLeaderId());
        fieldUtil.validateHaveOpponent(field);

        List<User> members = fieldUtil.getMembers(fieldId);
        List<Long> memberIds = members.stream().map(User::getId).collect(Collectors.toList());

        if (ids.contains(user.getId())){
            throw new BusinessException(BAD_REQUEST);
        }
        if (ids.stream().anyMatch(targetId -> !memberIds.contains(targetId))) {
            throw new BusinessException(NOT_MEMBER);
        }

        field.subtractMember(ids.size());
        userFieldRepository.deleteAllByFieldAndUserIdIn(field, ids);


        List<User> targetUsers = userRepository.findByIdIn(ids);
        targetUsers.forEach((User u) -> notificationService.sendUserNotification(EJECT, field, u));
    }

    @Transactional
    @Override
    public void exitField(User user, Long id) {
        Field field = fieldUtil.getField(id);
        fieldUtil.validateHaveOpponent(field);
        fieldUtil.validateIsMember(user, field);
        if(user.getId().equals(field.getLeaderId())){
            throw new BusinessException(MUST_NOT_LEADER);
        }
        userFieldRepository.deleteByFieldAndUser(field, user);
        field.subtractMember();
        battleEntryRepository.deleteAllByHostField(field);
        battleEntryRepository.deleteAllByEntrantField(field);

        notificationService.sendFieldNotification(EXIT, field, user.getName());
    }

    @Transactional
    @Override
    public void cheerMember(User user, Long id) {
        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));

        validateFcmTimeLimit(user, targetUser);

        notificationService.sendUserNotification(CHEER, user.getName(), targetUser);
    }

    @Override
    public void alertMembers(User user, Long id) {
        Field field = fieldUtil.getField(id);
        fieldUtil.validateIsMember(user, field);
        validateFcmTimeLimit(user, field);

        notificationService.sendFieldNotification(ALERT, field, user.getName());
    }

    @Override
    public void checkOwnBattle(User user) {
        List<UserField> myUserFields = userFieldRepository.findByUserAndStatusInAndTypeIn(user,
                List.of(RECRUITING, IN_PROGRESS), List.of(TEAM, TEAM_BATTLE));

        List<UserField> result = myUserFields.stream()
                .filter(userField -> {
                    Field field = userField.getField();
                    return field.getOpponent() == null;
                }).collect(Collectors.toList());

        if (result.isEmpty()){
            throw new BusinessException(SHOULD_CREATE);
        }
    }

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

    private void validateFcmTimeLimit(User fromUser, Object target) {
        String prefix;
        Long targetId;

        if (target instanceof User) {
            prefix = REDIS_CHEER_PREFIX;
            targetId = ((User) target).getId();
        } else {
            prefix = REDIS_WAKEUP_PREFIX;
            targetId = ((Field) target).getId();
        }
        String key = fromUser.getId() + prefix + targetId;

        if (REDIS_NOTIFICATION_VERIFIED.equals(redisService.getValues(key))) {
            throw new BusinessException(FCM_TIME_LIMIT);
        }

        redisService.setValues(key, REDIS_NOTIFICATION_VERIFIED, Duration.ofHours(2));
    }

    private FindAllMyFieldsDto getFindAllMyFieldsDto(User user, UserField userField) {
        FindAllMyFieldsDto findAllMyFieldsDto =
                userFieldMapper.toFindAllMyFieldsDto(userField.getField());
        findAllMyFieldsDto.setLeader(userField.getField().getLeaderId().equals(user.getId()));
        return findAllMyFieldsDto;
    }
}
