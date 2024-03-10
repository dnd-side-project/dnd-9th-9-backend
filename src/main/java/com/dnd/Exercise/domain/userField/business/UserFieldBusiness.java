package com.dnd.Exercise.domain.userField.business;

import static com.dnd.Exercise.global.common.Constants.REDIS_CHEER_PREFIX;
import static com.dnd.Exercise.global.common.Constants.REDIS_NOTIFICATION_VERIFIED;
import static com.dnd.Exercise.global.common.Constants.REDIS_WAKEUP_PREFIX;
import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FCM_TIME_LIMIT;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_MEMBER;

import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.userField.dto.AccumulatedActivityDto;
import com.dnd.Exercise.domain.userField.dto.AccumulatedExerciseDto;
import com.dnd.Exercise.domain.userField.dto.response.BattleStatusDto;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMyFieldsDto;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyTeamStatusRes;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.common.RedisService;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFieldBusiness {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final FieldBusiness fieldBusiness;
    private final UserFieldRepository userFieldRepository;

    public List<FindAllMyFieldsDto> getAllMyRecruitingFields(User user, List<UserField> myUserFields) {
        return myUserFields.stream()
                .filter(userField -> userField.getField().getOpponent() == null)
                .map(userField -> FindAllMyFieldsDto.from(userField.getField(), user.getId()))
                .collect(Collectors.toList());
    }

    public List<FindAllMyFieldsDto> getAllMyInProgressFields(List<UserField> inProgressUserFields) {
        return inProgressUserFields.stream()
                .filter(userField -> userField.getField().getOpponent() != null)
                .map(userField -> FindAllMyFieldsDto.from(userField.getField(), userField.getUser().getId()))
                .collect(Collectors.toList());
    }

    public List<FindAllMembersRes> getAllMembersRes(Long leaderId, List<UserField> allMembers) {
        return allMembers.stream().map(member -> FindAllMembersRes.from(member.getUser(), leaderId))
                .collect(Collectors.toList());
    }

    public String validateCheerTimeLimit(User fromUser, User toUser){
        String key = fromUser.getId() + REDIS_CHEER_PREFIX + toUser.getId();
        if (REDIS_NOTIFICATION_VERIFIED.equals(redisService.getValues(key))) {
            throw new BusinessException(FCM_TIME_LIMIT);
        }
        return key;
    }

    public String validateAlertTimeLimit(User fromUser, Field toField){
        String key = fromUser.getId() + REDIS_WAKEUP_PREFIX + toField.getId();
        if (REDIS_NOTIFICATION_VERIFIED.equals(redisService.getValues(key))) {
            throw new BusinessException(FCM_TIME_LIMIT);
        }
        return key;
    }

    public void validateNotEjectMyself(User user, List<Long> ids) {
        if (ids.contains(user.getId())) {
            throw new BusinessException(BAD_REQUEST);
        }
    }

    public void validateSelectedMembersPresent(Long fieldId, List<Long> ids) {
        List<Long> memberIds = fieldBusiness.getMemberIds(fieldId);
        if (!memberIds.containsAll(ids)) {
            throw new BusinessException(NOT_MEMBER);
        }
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
    }

    public FindMyTeamStatusRes createFindMyTeamStatusRes(UserField userField) {
        Field myField = userField.getField();
        List<Long> memberIds = fieldBusiness.getMemberIds(myField.getId());
        LocalDate startDate = myField.getStartDate();

        List<AccumulatedActivityDto> activityValues =
                userFieldRepository.findAccumulatedActivityValues(startDate, memberIds);
        List<AccumulatedExerciseDto> exerciseValues =
                userFieldRepository.findAccumulatedExerciseValues(startDate, memberIds);

        return FindMyTeamStatusRes.from(activityValues, exerciseValues, myField);
    }

    public FindMyBattleStatusRes createFindMyBattleStatusRes(UserField userField) {
        Field myField = userField.getField();
        Field opponentField = myField.getOpponent();

        BattleStatusDto home = getBattleStatusDto(myField);
        BattleStatusDto away = getBattleStatusDto(opponentField);

        return FindMyBattleStatusRes.from(home, away, myField);
    }

    public BattleStatusDto getBattleStatusDto(Field myField) {
        Long fieldId = myField.getId();
        LocalDate startDate = myField.getStartDate();

        List<Integer> score = fieldBusiness.getFieldSummary(fieldId, startDate, LocalDate.now());
        return BattleStatusDto.from(myField.getName(), score);
    }
}
