package com.dnd.Exercise.domain.fieldEntry.service;

import static com.dnd.Exercise.domain.field.entity.enums.FieldType.DUEL;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.TEAM_BATTLE;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_APPLY;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_FULL;
import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FIELD_NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FORBIDDEN;
import static com.dnd.Exercise.global.error.dto.ErrorCode.MUST_LEADER;
import static com.dnd.Exercise.global.error.dto.ErrorCode.PERIOD_NOT_MATCH;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.fieldEntry.dto.FieldEntryMapper;
import com.dnd.Exercise.domain.fieldEntry.dto.request.BattleFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection;
import com.dnd.Exercise.domain.fieldEntry.dto.request.TeamFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryDto;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryRes;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryDto;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryRes;
import com.dnd.Exercise.domain.fieldEntry.entity.FieldEntry;
import com.dnd.Exercise.domain.fieldEntry.repository.FieldEntryRepository;
import com.dnd.Exercise.domain.notification.entity.NotificationDto;
import com.dnd.Exercise.domain.notification.entity.NotificationTopic;
import com.dnd.Exercise.domain.notification.entity.NotificationType;
import com.dnd.Exercise.domain.notification.event.NotificationEvent;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.util.field.FieldUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    
    private void validateIsNotFull(Field field) {
        if(field.getCurrentSize() == field.getMaxSize()){
            throw new BusinessException(ALREADY_FULL);
        }
    }

    private Field getFieldByIdAndFieldType(Long fieldId, FieldType fieldType) {
        return fieldRepository.findByIdAndFieldType(fieldId, fieldType)
                .orElseThrow(() -> new BusinessException(FIELD_NOT_FOUND));
    }


    @Transactional
    @Override
    public void createTeamFieldEntry(User user, TeamFieldEntryReq fieldEntryReq) {
        FieldType fieldType = fieldEntryReq.getTeamType().toFieldType();
        Field hostField = getFieldByIdAndFieldType(fieldEntryReq.getTargetFieldId(), fieldType);

        fieldUtil.validateHaveOpponent(hostField);

        validateIsNotFull(hostField);

        if(fieldEntryRepository.existsByEntrantUserAndHostField(user, hostField)){
            throw new BusinessException(ALREADY_APPLY);
        }

        fieldUtil.validateNotHavingField(user, fieldType);

        FieldEntry fieldEntry = FieldEntry.builder()
                .entrantUser(user).hostField(hostField).fieldType(fieldType).build();
        fieldEntryRepository.save(fieldEntry);
    }

    @Transactional
    @Override
    public void createBattleFieldEntry(User user, BattleFieldEntryReq fieldEntryReq) {
        FieldType fieldType = fieldEntryReq.getBattleType().toFieldType();
        Field hostField = getFieldByIdAndFieldType(fieldEntryReq.getTargetFieldId(), fieldType);

        UserField myUserField = fieldUtil.validateHavingField(user, fieldType);
        
        Field myField = myUserField.getField();

        if(myField.equals(hostField)){
            throw new BusinessException(BAD_REQUEST);
        }

        fieldUtil.validateIsLeader(user.getId(), myField.getLeaderId());
        fieldUtil.validateHaveOpponent(myField);
        fieldUtil.validateIsFull(myField);

        fieldUtil.validateHaveOpponent(hostField);
        fieldUtil.validateIsFull(hostField);

        if(fieldEntryRepository.existsByEntrantFieldAndHostField(myField, hostField)){
            throw new BusinessException(ALREADY_APPLY);
        }

        if(!myField.getPeriod().equals(hostField.getPeriod())){
            throw new BusinessException(PERIOD_NOT_MATCH);
        }

        FieldEntry fieldEntry = FieldEntry.builder()
                .entrantField(myField).hostField(hostField).fieldType(fieldType).build();
        fieldEntryRepository.save(fieldEntry);
    }




    @Transactional
    @Override
    public void deleteFieldEntry(User user, Long entryId) {
        FieldEntry fieldEntry = fieldEntryRepository.findById(entryId)
                .orElseThrow(() -> new BusinessException(FIELD_NOT_FOUND));

        Field entrantField = fieldEntry.getEntrantField();
        Field hostField = fieldEntry.getHostField();


        if(fieldEntry.getEntrantField() == null){
            if(!fieldEntry.getEntrantUser().equals(user)
                    && !hostField.getLeaderId().equals(user.getId())){
                throw new BusinessException(BAD_REQUEST);
            }
        }else{
            if(!entrantField.getLeaderId().equals(user.getId())
                    && !hostField.getLeaderId().equals(user.getId())){
                throw new BusinessException(MUST_LEADER);
            }
        }
        fieldEntryRepository.deleteById(entryId);
    }

    @Transactional
    @Override
    public void acceptFieldEntry(User user, Long entryId) {
        FieldEntry fieldEntry = fieldEntryRepository.findById(entryId)
                .orElseThrow(() -> new BusinessException(FIELD_NOT_FOUND));

        Field entrantField = fieldEntry.getEntrantField();
        Field hostField = fieldEntry.getHostField();
        User entrantUser = fieldEntry.getEntrantUser();

        fieldUtil.validateIsLeader(user.getId(), hostField.getLeaderId());

        if(entrantField == null) {
            validateIsNotFull(hostField);
            hostField.addMember();
            UserField userField = new UserField(entrantUser, hostField);
            userFieldRepository.save(userField);

            fieldEntryRepository.deleteAllByEntrantUser(entrantUser);
            if (hostField.getCurrentSize() == hostField.getMaxSize()){
                fieldEntryRepository.deleteAllByHostFieldAndEntrantField(hostField, null);
            }

            NotificationDto userNotificationDto = NotificationDto.builder()
                    .topic(NotificationTopic.TEAM_ACCEPT)
                    .field(hostField)
                    .notificationType(NotificationType.USER)
                    .build();

            eventPublisher.publishEvent(new NotificationEvent(List.of(entrantUser), userNotificationDto));


            NotificationDto fieldNotificationDto = NotificationDto.builder()
                    .topic(NotificationTopic.TEAM_ACCEPT)
                    .field(hostField)
                    .name(entrantUser.getName())
                    .notificationType(NotificationType.FIELD)
                    .build();

            eventPublisher.publishEvent(new NotificationEvent(fieldUtil.getMembers(
                    hostField.getId()), fieldNotificationDto));
        }
        else{
            fieldUtil.validateIsFull(hostField);
            fieldUtil.validateIsFull(entrantField);

            entrantField.changeOpponent(hostField);
            fieldEntryRepository.deleteAllByEntrantField(entrantField);
            fieldEntryRepository.deleteAllByHostFieldAndEntrantUser(hostField, null);

            NotificationDto notificationDto = NotificationDto.builder()
                    .topic(NotificationTopic.BATTLE_ACCEPT)
                    .field(hostField)
                    .name(entrantField.getName())
                    .notificationType(NotificationType.FIELD)
                    .build();

            eventPublisher.publishEvent(new NotificationEvent(fieldUtil.getMembers(
                    hostField.getId()), notificationDto));

            NotificationDto opponentNotificationDto = NotificationDto.builder()
                    .topic(NotificationTopic.BATTLE_ACCEPT)
                    .field(entrantField)
                    .name(hostField.getName())
                    .notificationType(NotificationType.FIELD)
                    .build();

            eventPublisher.publishEvent(new NotificationEvent(fieldUtil.getMembers(
                    entrantField.getId()), opponentNotificationDto));
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
}
