package com.dnd.Exercise.domain.BattleEntry.service;

import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.BATTLE_ACCEPT;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_APPLY;
import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ENTRY_NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.PERIOD_NOT_MATCH;

import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesDto;
import com.dnd.Exercise.domain.BattleEntry.entity.BattleEntry;
import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesRes;
import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
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
public class BattleEntryServiceImpl implements BattleEntryService {

    private final FieldBusiness fieldBusiness;
    private final BattleEntryRepository battleEntryRepository;
    private final UserFieldRepository userFieldRepository;
    private final NotificationService notificationService;


    @Transactional
    @Override
    public void createBattleEntry(User user, Long fieldId) {
        Field hostField = fieldBusiness.getField(fieldId);
        Field myField = checkCreateBattleFieldEntryValidity(user, hostField);

        BattleEntry battleEntry = BattleEntry.from(myField, hostField);
        battleEntryRepository.save(battleEntry);
    }


    @Transactional
    @Override
    public void cancelBattleEntry(User user, Long entryId) {
        BattleEntry battleEntry = getBattleEntry(entryId);
        checkCancelBattleEntryValidity(user, battleEntry);

        battleEntryRepository.deleteById(entryId);
    }

    @Transactional
    @Override
    public void acceptBattleEntry(User user, Long entryId) {
        BattleEntry battleEntry = getBattleEntry(entryId);
        Field entrantField = battleEntry.getEntrantField();
        Field hostField = battleEntry.getHostField();

        checkAcceptBattleEntryValidity(user, entrantField, hostField);

        entrantField.changeOpponent(hostField);
        battleEntryRepository.deleteAllByEntrantFieldOrHostField(entrantField, hostField);

        notificationService.sendFieldNotification(BATTLE_ACCEPT, hostField, entrantField.getName());
        notificationService.sendFieldNotification(BATTLE_ACCEPT, entrantField, hostField.getName());
    }

    @Override
    public FindBattleEntriesRes findSentBattleEntries(User user, Long fieldId, Pageable pageable) {
        Field field = fieldBusiness.getField(fieldId);
        fieldBusiness.validateIsMember(user, field);

        Page<BattleEntry> battleEntryPage = battleEntryRepository.findByEntrantField(field, pageable);
        Page<FindBattleEntriesDto> battleEntriesDtoPage = battleEntryPage.map(FindBattleEntriesDto::toSentEntryDto);

        return FindBattleEntriesRes.from(battleEntriesDtoPage, pageable);
    }

    @Override
    public FindBattleEntriesRes findReceivedBattleEntries(User user, Long fieldId, Pageable pageable) {
        Field field = fieldBusiness.getField(fieldId);
        fieldBusiness.validateIsMember(user, field);

        Page<BattleEntry> battleEntryPage = battleEntryRepository.findByHostField(field, pageable);
        Page<FindBattleEntriesDto> battleEntriesDtoPage = battleEntryPage.map(FindBattleEntriesDto::toReceivedEntryDto);

        return FindBattleEntriesRes.from(battleEntriesDtoPage, pageable);
    }

    @Override
    public FindBattleEntriesRes findMySentBattleEntries(User user, BattleType battleType, Pageable pageable) {
        List<UserField> userFields = userFieldRepository.findAllByUser(user);

        UserField myUserField = getMyNotStartedUserFieldByType(battleType, userFields);
        if (myUserField == null) return new FindBattleEntriesRes(pageable);
        Field myField = myUserField.getField();

        Page<BattleEntry> battleEntryPage = battleEntryRepository.findByEntrantField(myField, pageable);
        Page<FindBattleEntriesDto> battleEntriesDtoPage = battleEntryPage.map(FindBattleEntriesDto::toSentEntryDto);

        return FindBattleEntriesRes.from(battleEntriesDtoPage, pageable);
    }
    

    private Field checkCreateBattleFieldEntryValidity(User user, Field hostField) {
        FieldType fieldType = hostField.getFieldType();
        UserField myUserField = fieldBusiness.validateHavingField(user, fieldType);
        Field myField = myUserField.getField();

        validateIsMyField(hostField, myField);
        fieldBusiness.validateIsLeader(user.getId(), myField.getLeaderId());
        fieldBusiness.validateHaveOpponent(myField);
        fieldBusiness.validateIsFull(myField);

        fieldBusiness.validateHaveOpponent(hostField);
        fieldBusiness.validateIsFull(hostField);

        validateDuplicateBattleApply(hostField, myField);
        validateSamePeriod(hostField, myField);
        return myField;
    }
    
    private UserField getMyNotStartedUserFieldByType(BattleType battleType, List<UserField> userFields) {
        return userFields.stream().filter(userField -> filterFullAndOpponentNull(battleType, userField))
                .findFirst().orElse(null);
    }

    private boolean filterFullAndOpponentNull(BattleType battleType, UserField userField) {
        Field field = userField.getField();
        return field.getFieldType().equals(battleType.toFieldType())
                && field.getCurrentSize() == field.getMaxSize()
                && field.getOpponent() == null;
    }

    private void validateDuplicateBattleApply(Field hostField, Field myField) {
        if(battleEntryRepository.existsByEntrantFieldAndHostField(myField, hostField)){
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

    private void checkCancelBattleEntryValidity(User user, BattleEntry battleEntry) {
        Long entrantLeaderId = battleEntry.getEntrantField().getLeaderId();
        Long userId = user.getId();
        fieldBusiness.validateIsLeader(userId, entrantLeaderId);
    }

    private void checkAcceptBattleEntryValidity(User user, Field entrantField, Field hostField) {
        fieldBusiness.validateIsLeader(user.getId(), hostField.getLeaderId());
        fieldBusiness.validateIsFull(hostField);
        fieldBusiness.validateIsFull(entrantField);
    }

    private BattleEntry getBattleEntry(Long entryId) {
        return battleEntryRepository.findById(entryId)
                .orElseThrow(() -> new BusinessException(ENTRY_NOT_FOUND));
    }
}
