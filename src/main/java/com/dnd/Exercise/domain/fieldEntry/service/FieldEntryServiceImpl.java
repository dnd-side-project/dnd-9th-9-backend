package com.dnd.Exercise.domain.fieldEntry.service;

import static com.dnd.Exercise.domain.field.entity.FieldStatus.IN_PROGRESS;
import static com.dnd.Exercise.domain.field.entity.FieldStatus.RECRUITING;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_APPLY;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_FULL;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_IN_PROGRESS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static com.dnd.Exercise.global.error.dto.ErrorCode.FORBIDDEN;
import static com.dnd.Exercise.global.error.dto.ErrorCode.HAVING_IN_PROGRESS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.PERIOD_NOT_MATCH;
import static com.dnd.Exercise.global.error.dto.ErrorCode.RECRUITING_YET;
import static com.dnd.Exercise.global.error.dto.ErrorCode.SHOULD_CREATE;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.fieldEntry.dto.request.BattleFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.request.TeamFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.entity.FieldEntry;
import com.dnd.Exercise.domain.fieldEntry.repository.FieldEntryRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    @Override
    public void createTeamFieldEntry(User user, TeamFieldEntryReq fieldEntryReq) {
        FieldType fieldType = fieldEntryReq.getTeamType().toFieldType();
        Field hostField = fieldRepository.findByIdAndFieldType(fieldEntryReq.getTargetFieldId(), fieldType)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));

        if(!RECRUITING.equals(hostField.getFieldStatus())){
            throw new BusinessException(ALREADY_IN_PROGRESS);
        }

        if(hostField.getCurrentSize() == hostField.getMaxSize()){
            throw new BusinessException(ALREADY_FULL);
        }

        if(fieldEntryRepository.existsByEntrantUserAndHostField(user, hostField)){
            throw new BusinessException(ALREADY_APPLY);
        }

        if(userFieldRepository.findByUserAndStatusAndType(user, List.of(IN_PROGRESS, RECRUITING),
                fieldType).isPresent()){
            throw new BusinessException(HAVING_IN_PROGRESS);
        }

        FieldEntry fieldEntry = FieldEntry.builder()
                .entrantUser(user).hostField(hostField).fieldType(fieldType).build();
        fieldEntryRepository.save(fieldEntry);
    }

    @Transactional
    @Override
    public void createBattleFieldEntry(User user, BattleFieldEntryReq fieldEntryReq) {
        FieldType fieldType = fieldEntryReq.getBattleType().toFieldType();
        Field hostField = fieldRepository.findByIdAndFieldType(fieldEntryReq.getTargetFieldId(), fieldType)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));

        UserField myUserField = userFieldRepository.findByUserAndStatusAndType(
                        user, List.of(IN_PROGRESS, RECRUITING), fieldType)
                .orElseThrow(() -> new BusinessException(SHOULD_CREATE));
        Field myField = myUserField.getField();

        if(myField.equals(hostField)){
            throw new BusinessException(BAD_REQUEST);
        }

        if(!user.getId().equals(myField.getLeaderId())){
            throw new BusinessException(FORBIDDEN);
        }

        if(!RECRUITING.equals(myField.getFieldStatus())){
            throw new BusinessException(ALREADY_IN_PROGRESS);
        }

        if(myField.getCurrentSize() != myField.getMaxSize()){
            throw new BusinessException(RECRUITING_YET);
        }

        if(!RECRUITING.equals(hostField.getFieldStatus())){
            throw new BusinessException(ALREADY_IN_PROGRESS);
        }

        if(hostField.getCurrentSize() != hostField.getMaxSize()){
            throw new BusinessException(RECRUITING_YET);
        }

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
                .orElseThrow(() -> new BusinessException(NOT_FOUND));

        Field entrantField = fieldEntry.getEntrantField();
        Field hostField = fieldEntry.getHostField();


        if(fieldEntry.getEntrantField() == null){
            if(!fieldEntry.getEntrantUser().equals(user) || !hostField.getLeaderId().equals(user.getId())){
                throw new BusinessException(BAD_REQUEST);
            }
        }else{
            if(!entrantField.getLeaderId().equals(user.getId()) || !hostField.getLeaderId().equals(user.getId())){
                throw new BusinessException(FORBIDDEN);
            }
        }
        fieldEntryRepository.deleteById(entryId);
    }

    @Transactional
    @Override
    public void acceptFieldEntry(User user, Long entryId) {
        FieldEntry fieldEntry = fieldEntryRepository.findById(entryId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));

        Field entrantField = fieldEntry.getEntrantField();
        Field hostField = fieldEntry.getHostField();
        User entrantUser = fieldEntry.getEntrantUser();

        if(!hostField.getLeaderId().equals(user.getId())){
            throw new BusinessException(FORBIDDEN);
        }

        if(entrantField == null) {
            if (hostField.getCurrentSize() == hostField.getMaxSize()){
                throw new BusinessException(ALREADY_FULL);
            }
            hostField.addMember();
            UserField userField = new UserField(entrantUser, hostField);
            userFieldRepository.save(userField);

            fieldEntryRepository.deleteAllByEntrantUser(entrantUser);
        }
        else{
            entrantField.changeOpponent(hostField);
            fieldEntryRepository.deleteAllByEntrantField(entrantField);
        }
    }
}
