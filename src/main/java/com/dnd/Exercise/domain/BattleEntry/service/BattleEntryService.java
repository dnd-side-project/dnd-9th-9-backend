package com.dnd.Exercise.domain.BattleEntry.service;

import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesRes;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface BattleEntryService {

    void createBattleEntry(User user, Long fieldId);

    void cancelBattleEntry(User user, Long entryId);

    void acceptBattleEntry(User user, Long entryId);

    FindBattleEntriesRes findSentBattleEntries(User user, Long fieldId, Pageable pageable);

    FindBattleEntriesRes findReceivedBattleEntries(User user, Long fieldId, Pageable pageable);

    FindBattleEntriesRes findMySentBattleEntries(User user, BattleType battleType, Pageable pageable);
}
