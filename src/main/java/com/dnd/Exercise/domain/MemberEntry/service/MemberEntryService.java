package com.dnd.Exercise.domain.MemberEntry.service;

import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesRes;
import com.dnd.Exercise.domain.MemberEntry.dtos.response.FindMemberEntriesRes;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface MemberEntryService {
    void createMemberEntry(User user, Long fieldId);
    void cancelMemberEntry(User user, Long entryId);
    void acceptMemberEntry(User user, Long entryId);
    FindMemberEntriesRes findReceivedMemberEntries(User user, Long fieldId, Pageable pageable);
    FindBattleEntriesRes findMySentMemberEntries(User user, Pageable pageable);
}
