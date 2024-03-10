package com.dnd.Exercise.domain.userField.service;

import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.*;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.*;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.ALERT;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.CHEER;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.EJECT;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.EXIT;
import static com.dnd.Exercise.global.common.Constants.REDIS_NOTIFICATION_VERIFIED;

import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.userField.business.UserFieldBusiness;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMyCompletedFieldsRes;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMyFieldsDto;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyTeamStatusRes;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.common.RedisService;
import java.time.Duration;
import java.util.List;
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
    private final UserFieldBusiness userFieldBusiness;

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

        field.subtractMember();
        userFieldRepository.deleteByFieldAndUser(field, user);

        clearBattleEntries(field);
        notificationService.sendFieldNotification(EXIT, field, user.getName());
    }

    @Transactional
    @Override
    public void cheerMember(User user, Long id) {
        User targetUser = userFieldBusiness.getUser(id);
        String key = userFieldBusiness.validateCheerTimeLimit(user, targetUser);

        redisService.setValues(key, REDIS_NOTIFICATION_VERIFIED, Duration.ofHours(2));
        notificationService.sendUserNotification(CHEER, user.getName(), targetUser);
    }

    @Override
    public void alertMembers(User user, Long id) {
        Field field = fieldBusiness.getField(id);
        fieldBusiness.validateIsMember(user, field);
        String key = userFieldBusiness.validateAlertTimeLimit(user, field);

        redisService.setValues(key, REDIS_NOTIFICATION_VERIFIED, Duration.ofHours(2));
        notificationService.sendFieldNotification(ALERT, field, user.getName());
    }

    @Override
    public FindMyBattleStatusRes findMyBattleStatus(User user, BattleType battleType) {
        return userFieldRepository.findByUserAndStatusAndType(user, IN_PROGRESS, battleType.toFieldType())
                .map(userFieldBusiness::createFindMyBattleStatusRes)
                .orElse(null);
    }

    @Override
    public FindMyTeamStatusRes findMyTeamStatus(User user) {
        return userFieldRepository.findByUserAndStatusAndType(user, IN_PROGRESS, TEAM)
                .map(userFieldBusiness::createFindMyTeamStatusRes)
                .orElse(null);
    }

    @Override
    public List<FindAllMembersRes> findAllMembers(Long fieldId) {
        Field field = fieldBusiness.getField(fieldId);
        Long leaderId = field.getLeaderId();
        List<UserField> allMembers = userFieldRepository.findAllByFieldId(fieldId);

        return userFieldBusiness.getAllMembersRes(leaderId, allMembers);
    }

    @Override
    public List<FindAllMyFieldsDto> findAllMyRecruitingFields(User user) {
        List<UserField> myUserFields = userFieldRepository.findByUserAndStatus(user, RECRUITING);
        return userFieldBusiness.getAllMyRecruitingFields(user, myUserFields);
    }

    @Override
    public List<FindAllMyFieldsDto> findAllMyInProgressFields(User user) {
        List<UserField> inProgressUserFields = userFieldRepository.findByUserAndStatusIn(
                user, List.of(RECRUITING, IN_PROGRESS));

        return userFieldBusiness.getAllMyInProgressFields(inProgressUserFields);
    }

    @Override
    public FindAllMyCompletedFieldsRes findAllMyCompletedFields(User user, FieldType fieldType, Pageable pageable) {
        Page<UserField> userFieldPage = userFieldRepository.findCompletedFieldByUserAndType(user, fieldType, pageable);
        Page<FindAllMyFieldsDto> findAllMyFieldsDtoPage = userFieldPage.map(userField ->
                FindAllMyFieldsDto.from(userField.getField(), user.getId()));

        return FindAllMyCompletedFieldsRes.from(findAllMyFieldsDtoPage, pageable);
    }


    private void checkEjectMemberValidity(User user, Field field, List<Long> ids) {
        field.validateIsLeader(user.getId());
        field.validateHaveOpponent();
        userFieldBusiness.validateNotEjectMyself(user, ids);
        userFieldBusiness.validateSelectedMembersPresent(field.getId(), ids);
    }

    private void checkExitFieldValidity(User user, Field field) {
        field.validateHaveOpponent();
        fieldBusiness.validateIsMember(user, field);
        field.validateIsNotLeader(user);
    }

    private void clearBattleEntries(Field field) {
        battleEntryRepository.deleteAllByHostField(field);
        battleEntryRepository.deleteAllByEntrantField(field);
    }
}
