package com.dnd.Exercise.domain.fieldEntry.service;

import static com.dnd.Exercise.domain.field.entity.enums.FieldType.DUEL;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.TEAM_BATTLE;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.BATTLE_ACCEPT;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.TEAM_ACCEPT;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_APPLY;
import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FIELD_NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.MUST_LEADER;
import static com.dnd.Exercise.global.error.dto.ErrorCode.PERIOD_NOT_MATCH;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.fieldEntry.dto.FieldEntryMapper;
import com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryDto;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryRes;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryDto;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryRes;
import com.dnd.Exercise.domain.fieldEntry.entity.FieldEntry;
import com.dnd.Exercise.domain.fieldEntry.repository.FieldEntryRepository;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.util.field.FieldUtil;
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
public class FieldEntryServiceImpl implements FieldEntryService {

    private final FieldEntryRepository fieldEntryRepository;
    private final FieldRepository fieldRepository;
    private final UserFieldRepository userFieldRepository;
    private final FieldEntryMapper fieldEntryMapper;
    private final FieldUtil fieldUtil;
    private final NotificationService notificationService;

    



    @Transactional
    @Override
    public void createTeamFieldEntry(User user, Long fieldId) {
        Field hostField = fieldUtil.getField(fieldId);
        FieldType fieldType = checkCreateTeamFieldEntryValidity(user, hostField);
        FieldEntry fieldEntry = FieldEntry.builder()
                .entrantUser(user)
                .hostField(hostField)
                .fieldType(fieldType)
                .build();

        fieldEntryRepository.save(fieldEntry);
    }

    @Transactional
    @Override
    public void createBattleFieldEntry(User user, Long fieldId) {
        Field hostField = fieldUtil.getField(fieldId);
        FieldType fieldType = hostField.getFieldType();

        Field myField = checkCreateBattleFieldEntryValidity(user, hostField, fieldType);
        FieldEntry fieldEntry = FieldEntry.builder()
                .entrantField(myField)
                .hostField(hostField)
                .fieldType(fieldType)
                .build();

        fieldEntryRepository.save(fieldEntry);
    }


    @Transactional
    @Override
    public void deleteFieldEntry(User user, Long entryId) {
        FieldEntry fieldEntry = getFieldEntry(entryId);

        Field entrantField = fieldEntry.getEntrantField();
        User entrantUser = fieldEntry.getEntrantUser();

        if(entrantUser != null) checkDeleteTeamEntryValidity(user, fieldEntry);
        else if(entrantField != null) checkDeleteBattleEntryValidity(user, fieldEntry);
        fieldEntryRepository.deleteById(entryId);
    }

    @Transactional
    @Override
    public void acceptFieldEntry(User user, Long entryId) {
        FieldEntry fieldEntry = getFieldEntry(entryId);

        Field entrantField = fieldEntry.getEntrantField();
        Field hostField = fieldEntry.getHostField();
        User entrantUser = fieldEntry.getEntrantUser();

        fieldUtil.validateIsLeader(user.getId(), hostField.getLeaderId());

        if(entrantField == null) {
            fieldUtil.validateIsNotFull(hostField);
            hostField.addMember();
            UserField userField = new UserField(entrantUser, hostField);
            userFieldRepository.save(userField);

            fieldEntryRepository.deleteAllByEntrantUser(entrantUser);
            if (hostField.getCurrentSize() == hostField.getMaxSize()){
                fieldEntryRepository.deleteAllByHostFieldAndEntrantField(hostField, null);
            }

            notificationService.sendUserNotification(TEAM_ACCEPT, hostField, entrantUser);
            notificationService.sendFieldNotification(TEAM_ACCEPT, hostField, entrantUser.getName());
        }
        else{
            fieldUtil.validateIsFull(hostField);
            fieldUtil.validateIsFull(entrantField);

            entrantField.changeOpponent(hostField);
            fieldEntryRepository.deleteAllByEntrantField(entrantField);
            fieldEntryRepository.deleteAllByHostFieldAndEntrantUser(hostField, null);

            notificationService.sendFieldNotification(BATTLE_ACCEPT, hostField, entrantField.getName());
            notificationService.sendFieldNotification(BATTLE_ACCEPT, entrantField, hostField.getName());
        }
    }

    @Override
    public FindAllTeamEntryRes findAllTeamEntries(User user, Long fieldId,
            Pageable pageable) {
        Field field = fieldUtil.getField(fieldId);
        fieldUtil.validateIsMember(user, field);

        Page<FindAllTeamEntryDto> queryResult = fieldEntryRepository
                .findAllTeamEntryByHostField(field, pageable);

        List<FindAllTeamEntryDto> teamEntries = queryResult.getContent();
        Long totalCount = queryResult.getTotalElements();

        return FindAllTeamEntryRes.builder()
                .teamEntries(teamEntries)
                .totalCount(totalCount)
                .currentPageSize(pageable.getPageSize())
                .currentPageNumber(pageable.getPageNumber())
                .build();
    }

    @Override
    public FindAllBattleEntryRes findAllBattleEntriesByDirection(User user, Long fieldId,
            FieldDirection fieldDirection, Pageable pageable) {
        Field field = fieldUtil.getField(fieldId);
        fieldUtil.validateIsMember(user, field);

        Page<FindAllBattleEntryDto> queryResult = fieldEntryRepository
                .findAllBattleByField(field, fieldDirection, pageable);

        List<FindAllBattleEntryDto> battleEntries = queryResult.getContent();
        Long totalCount = queryResult.getTotalElements();

        return FindAllBattleEntryRes.builder()
                .battleEntries(battleEntries)
                .totalCount(totalCount)
                .currentPageSize(pageable.getPageSize())
                .currentPageNumber(pageable.getPageNumber())
                .build();
        
    }

    @Override
    public FindAllBattleEntryRes findAllBattleEntriesByType(User user, FieldType fieldType,
            Pageable pageable) {
        Page<FieldEntry> queryResult = null;
        if(List.of(DUEL, TEAM_BATTLE).contains(fieldType)){
            List<UserField> userFields = userFieldRepository.findAllByUser(user);

            UserField myUserField = userFields.stream().filter(userField -> {
                Field field = userField.getField();
                return field.getFieldType().equals(fieldType)
                        && field.getOpponent() == null
                        && field.getCurrentSize() == field.getMaxSize();
            }).findFirst().orElse(null);

            if(myUserField == null){
                return new FindAllBattleEntryRes(pageable);
            }

            queryResult = fieldEntryRepository.findByEntrantField(myUserField.getField(), pageable);
        } else {
            queryResult = fieldEntryRepository.findByEntrantUser(user, pageable);
        }

        List<FieldEntry> entryList = queryResult.getContent();
        Long totalCount = queryResult.getTotalElements();

        List<FindAllBattleEntryDto> battleEntries = entryList.stream()
                .map(fieldEntryMapper::toFindAllBattleEntryRes).collect(
                        Collectors.toList());

        return FindAllBattleEntryRes.builder()
                .battleEntries(battleEntries)
                .totalCount(totalCount)
                .currentPageSize(pageable.getPageSize())
                .currentPageNumber(pageable.getPageNumber())
                .build();
    }

    private void validateDuplicateTeamApply(User user, Field hostField) {
        if(fieldEntryRepository.existsByEntrantUserAndHostField(user, hostField)){
            throw new BusinessException(ALREADY_APPLY);
        }
    }

    private void validateDuplicateBattleApply(Field hostField, Field myField) {
        if(fieldEntryRepository.existsByEntrantFieldAndHostField(myField, hostField)){
            throw new BusinessException(ALREADY_APPLY);
        }
    }

    private void validateSamePeriod(Field hostField, Field myField) {
        if(!myField.getPeriod().equals(hostField.getPeriod())){
            throw new BusinessException(PERIOD_NOT_MATCH);
        }
    }

    private void validateIsMyField(Field hostField, Field myField) {
        if(myField.equals(hostField)){
            throw new BusinessException(BAD_REQUEST);
        }
    }

    private FieldType checkCreateTeamFieldEntryValidity(User user, Field hostField) {
        FieldType fieldType = hostField.getFieldType();

        fieldUtil.validateHaveOpponent(hostField);
        fieldUtil.validateIsNotFull(hostField);
        validateDuplicateTeamApply(user, hostField);
        fieldUtil.validateNotHavingField(user, fieldType);
        return fieldType;
    }

    private Field checkCreateBattleFieldEntryValidity(User user, Field hostField, FieldType fieldType) {
        UserField myUserField = fieldUtil.validateHavingField(user, fieldType);
        Field myField = myUserField.getField();

        validateIsMyField(hostField, myField);
        fieldUtil.validateIsLeader(user.getId(), myField.getLeaderId());
        fieldUtil.validateHaveOpponent(myField);
        fieldUtil.validateIsFull(myField);

        fieldUtil.validateHaveOpponent(hostField);
        fieldUtil.validateIsFull(hostField);

        validateDuplicateBattleApply(hostField, myField);
        validateSamePeriod(hostField, myField);
        return myField;
    }

    private void checkDeleteBattleEntryValidity(User user, FieldEntry fieldEntry) {
        Long entrantLeaderId = fieldEntry.getEntrantField().getLeaderId();
        Long hostLeaderId = fieldEntry.getHostField().getLeaderId();
        Long userId = user.getId();

        if(!entrantLeaderId.equals(userId) && !hostLeaderId.equals(userId)){
            throw new BusinessException(MUST_LEADER);
        }
    }

    private void checkDeleteTeamEntryValidity(User user, FieldEntry fieldEntry) {
        Long entrantUserId = fieldEntry.getEntrantUser().getId();
        Long hostLeaderId = fieldEntry.getHostField().getLeaderId();
        Long userId = user.getId();

        if(!entrantUserId.equals(userId) && !hostLeaderId.equals(userId)){
            throw new BusinessException(BAD_REQUEST);
        }
    }

    private FieldEntry getFieldEntry(Long entryId) {
        return fieldEntryRepository.findById(entryId)
                .orElseThrow(() -> new BusinessException(FIELD_NOT_FOUND));
    }
}
