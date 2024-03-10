package com.dnd.Exercise.domain.BattleEntry.service;

import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.BATTLE_ACCEPT;

import com.dnd.Exercise.domain.BattleEntry.business.BattleEntryBusiness;
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
    private final BattleEntryBusiness battleEntryBusiness;


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
        BattleEntry battleEntry = battleEntryBusiness.getBattleEntry(entryId);
        checkCancelBattleEntryValidity(user, battleEntry);

        battleEntryRepository.deleteById(entryId);
    }

    @Transactional
    @Override
    public void acceptBattleEntry(User user, Long entryId) {
        BattleEntry battleEntry = battleEntryBusiness.getBattleEntry(entryId);
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
        UserField myUserField = battleEntryBusiness.getMyNotStartedUserFieldByType(battleType, userFields);

        if (myUserField == null) return new FindBattleEntriesRes(pageable);

        Page<BattleEntry> battleEntryPage = battleEntryRepository.findByEntrantField(myUserField.getField(), pageable);
        Page<FindBattleEntriesDto> battleEntriesDtoPage = battleEntryPage.map(FindBattleEntriesDto::toSentEntryDto);

        return FindBattleEntriesRes.from(battleEntriesDtoPage, pageable);
    }
    

    private Field checkCreateBattleFieldEntryValidity(User user, Field hostField) {
        FieldType fieldType = hostField.getFieldType();
        UserField myUserField = fieldBusiness.validateHavingField(user, fieldType);
        Field myField = myUserField.getField();

        myField.validateIsMyField(hostField);
        myField.validateIsLeader(user.getId());
        myField.validateHaveOpponent();
        myField.validateIsFull();

        hostField.validateHaveOpponent();
        hostField.validateIsFull();

        battleEntryBusiness.validateDuplicateBattleApply(hostField, myField);
        myField.validateSamePeriod(hostField);
        return myField;
    }

    private void checkCancelBattleEntryValidity(User user, BattleEntry battleEntry) {
        Field entrantField = battleEntry.getEntrantField();
        entrantField.validateIsLeader(user.getId());
    }

    private void checkAcceptBattleEntryValidity(User user, Field entrantField, Field hostField) {
        hostField.validateIsLeader(user.getId());
        hostField.validateIsFull();
        entrantField.validateIsFull();
    }
}
