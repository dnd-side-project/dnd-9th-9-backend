package com.dnd.Exercise.domain.BattleEntry.business;

import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_APPLY;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ENTRY_NOT_FOUND;

import com.dnd.Exercise.domain.BattleEntry.entity.BattleEntry;
import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BattleEntryBusiness {

    private final BattleEntryRepository battleEntryRepository;

    public void validateDuplicateBattleApply(Field hostField, Field myField) {
        if(battleEntryRepository.existsByEntrantFieldAndHostField(myField, hostField)){
            throw new BusinessException(ALREADY_APPLY);
        }
    }

    public BattleEntry getBattleEntry(Long entryId) {
        return battleEntryRepository.findById(entryId)
                .orElseThrow(() -> new BusinessException(ENTRY_NOT_FOUND));
    }

    public UserField getMyNotStartedUserFieldByType(BattleType battleType, List<UserField> userFields) {
        return userFields.stream().filter(userField -> filterFullAndOpponentNull(battleType, userField))
                .findFirst().orElse(null);
    }

    public boolean filterFullAndOpponentNull(BattleType battleType, UserField userField) {
        Field field = userField.getField();
        return field.getFieldType().equals(battleType.toFieldType())
                && field.getCurrentSize() == field.getMaxSize()
                && field.getOpponent() == null;
    }
}
