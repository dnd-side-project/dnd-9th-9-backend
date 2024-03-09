package com.dnd.Exercise.domain.userField.service;

import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.*;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.*;
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

import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.userField.dto.AccumulatedActivityDto;
import com.dnd.Exercise.domain.userField.dto.AccumulatedExerciseDto;
import com.dnd.Exercise.domain.userField.dto.response.BattleStatusDto;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMyCompletedFieldsRes;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMyFieldsDto;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyTeamStatusRes;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.common.RedisService;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.time.Duration;
import java.time.LocalDate;
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
    private final UserRepository userRepository;
    private final BattleEntryRepository battleEntryRepository;
    private final FieldBusiness fieldBusiness;
    private final NotificationService notificationService;
    private final RedisService redisService;
    

    @Override
    public List<FindAllMembersRes> findAllMembers(Long fieldId) {
        Field field = fieldBusiness.getField(fieldId);
        Long leaderId = field.getLeaderId();
        List<UserField> allMembers = userFieldRepository.findAllByFieldId(fieldId);

        return allMembers.stream().map(member -> FindAllMembersRes.from(member.getUser(), leaderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<FindAllMyFieldsDto> findAllMyRecruitingFields(User user) {
        List<UserField> myUserFields = userFieldRepository.findByUserAndStatus(user, RECRUITING);

        return myUserFields.stream()
                .filter(userField -> userField.getField().getOpponent() == null)
                .map(userField -> FindAllMyFieldsDto.from(userField.getField(), user.getId()))
                .collect(Collectors.toList());
    }


    @Override
    public List<FindAllMyFieldsDto> findAllMyInProgressFields(User user) {
        List<UserField> inProgressUserFields = userFieldRepository.findByUserAndStatusIn(user,
                List.of(RECRUITING, IN_PROGRESS));

        return inProgressUserFields.stream()
                .filter(userField -> userField.getField().getOpponent() != null)
                .map(userField -> FindAllMyFieldsDto.from(userField.getField(), userField.getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public FindAllMyCompletedFieldsRes findAllMyCompletedFields(User user, FieldType fieldType, Pageable pageable) {
        Page<UserField> userFieldPage = userFieldRepository.findCompletedFieldByUserAndType(user, fieldType, pageable);
        Page<FindAllMyFieldsDto> findAllMyFieldsDtoPage = userFieldPage.map(userField ->
                FindAllMyFieldsDto.from(userField.getField(), user.getId()));

        return FindAllMyCompletedFieldsRes.from(findAllMyFieldsDtoPage, pageable);
    }

    @Override
    public FindMyBattleStatusRes findMyBattleStatus(User user, BattleType battleType) {
        return userFieldRepository.findByUserAndStatusAndType(user, IN_PROGRESS, battleType.toFieldType())
                .map(this::createFindMyBattleStatusRes)
                .orElse(null);
    }

    @Override
    public FindMyTeamStatusRes findMyTeamStatus(User user) {
        return userFieldRepository.findByUserAndStatusAndType(user, IN_PROGRESS, TEAM)
                .map(this::createFindMyTeamStatusRes)
                .orElse(null);
    }

    @Transactional
    @Override
    public void ejectMember(User user, Long fieldId, List<Long> ids) {
        Field field = fieldBusiness.getField(fieldId);
        checkEjectMemberValidity(user, field, ids);

        field.subtractMember(ids.size());
        userFieldRepository.deleteAllByFieldAndUserIdIn(field, ids);

        List<User> targetUsers = userRepository.findByIdIn(ids);
        targetUsers.forEach(u -> notificationService.sendUserNotification(EJECT, field, u));
    }

    @Transactional
    @Override
    public void exitField(User user, Long id) {
        Field field = fieldBusiness.getField(id);
        checkExitFieldValidity(user, field);
        removeUserFromField(user, field);
        clearBattleEntries(field);

        notificationService.sendFieldNotification(EXIT, field, user.getName());
    }


    @Transactional
    @Override
    public void cheerMember(User user, Long id) {
        User targetUser = getUser(id);
        String key = validateCheerTimeLimit(user, targetUser);
        redisService.setValues(key, REDIS_NOTIFICATION_VERIFIED, Duration.ofHours(2));

        notificationService.sendUserNotification(CHEER, user.getName(), targetUser);
    }

    @Override
    public void alertMembers(User user, Long id) {
        Field field = fieldBusiness.getField(id);
        fieldBusiness.validateIsMember(user, field);

        String key = validateAlertTimeLimit(user, field);
        redisService.setValues(key, REDIS_NOTIFICATION_VERIFIED, Duration.ofHours(2));

        notificationService.sendFieldNotification(ALERT, field, user.getName());
    }

    private FindMyTeamStatusRes createFindMyTeamStatusRes(UserField userField) {
        Field myField = userField.getField();
        List<Long> memberIds = fieldBusiness.getMemberIds(myField.getId());
        LocalDate startDate = myField.getStartDate();

        List<AccumulatedActivityDto> activityValues =
                userFieldRepository.findAccumulatedActivityValues(startDate, memberIds);
        List<AccumulatedExerciseDto> exerciseValues =
                userFieldRepository.findAccumulatedExerciseValues(startDate, memberIds);

        return FindMyTeamStatusRes.from(activityValues, exerciseValues, myField);
    }

    private FindMyBattleStatusRes createFindMyBattleStatusRes(UserField userField) {
        Field myField = userField.getField();
        Field opponentField = myField.getOpponent();

        BattleStatusDto home = getBattleStatusDto(myField);
        BattleStatusDto away = getBattleStatusDto(opponentField);

        return FindMyBattleStatusRes.from(home, away, myField);
    }

    private BattleStatusDto getBattleStatusDto(Field myField) {
        Long fieldId = myField.getId();
        LocalDate startDate = myField.getStartDate();

        List<Integer> score = fieldBusiness.getFieldSummary(fieldId, startDate, LocalDate.now());
        BattleStatusDto battleStatusDto = BattleStatusDto.from(myField.getName(), score);
        return battleStatusDto;
    }

    private void checkEjectMemberValidity(User user, Field field, List<Long> ids) {
        field.validateIsLeader(user.getId());
        field.validateHaveOpponent();
        validateUserNotEjectingThemselves(user, ids);
        validateSelectedMembersPresent(field.getId(), ids);
    }

    private void validateUserNotEjectingThemselves(User user, List<Long> ids) {
        if (ids.contains(user.getId())) {
            throw new BusinessException(BAD_REQUEST);
        }
    }

    private void validateSelectedMembersPresent(Long fieldId, List<Long> ids) {
        List<Long> memberIds = fieldBusiness.getMemberIds(fieldId);
        if (!memberIds.containsAll(ids)) {
            throw new BusinessException(NOT_MEMBER);
        }
    }

    private void checkExitFieldValidity(User user, Field field) {
        field.validateHaveOpponent();
        fieldBusiness.validateIsMember(user, field);
        validateIsNotLeader(user, field);
    }

    private void validateIsNotLeader(User user, Field field) {
        if(user.getId().equals(field.getLeaderId())){
            throw new BusinessException(MUST_NOT_LEADER);
        }
    }

    private void removeUserFromField(User user, Field field) {
        userFieldRepository.deleteByFieldAndUser(field, user);
        field.subtractMember();
    }

    private void clearBattleEntries(Field field) {
        battleEntryRepository.deleteAllByHostField(field);
        battleEntryRepository.deleteAllByEntrantField(field);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
    }

    private String validateCheerTimeLimit(User fromUser, User toUser){
        String key = fromUser.getId() + REDIS_CHEER_PREFIX + toUser.getId();
        if (REDIS_NOTIFICATION_VERIFIED.equals(redisService.getValues(key))) {
            throw new BusinessException(FCM_TIME_LIMIT);
        }
        return key;
    }

    private String validateAlertTimeLimit(User fromUser, Field toField){
        String key = fromUser.getId() + REDIS_WAKEUP_PREFIX + toField.getId();
        if (REDIS_NOTIFICATION_VERIFIED.equals(redisService.getValues(key))) {
            throw new BusinessException(FCM_TIME_LIMIT);
        }
        return key;
    }

}
