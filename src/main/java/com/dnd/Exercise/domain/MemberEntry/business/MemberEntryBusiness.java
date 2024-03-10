package com.dnd.Exercise.domain.MemberEntry.business;

import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_APPLY;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ENTRY_NOT_FOUND;

import com.dnd.Exercise.domain.MemberEntry.entity.MemberEntry;
import com.dnd.Exercise.domain.MemberEntry.repository.MemberEntryRepository;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEntryBusiness {

    private final MemberEntryRepository memberEntryRepository;

    public void validateDuplicateTeamApply(User user, Field hostField) {
        if(memberEntryRepository.existsByEntrantUserAndHostField(user, hostField)){
            throw new BusinessException(ALREADY_APPLY);
        }
    }

    public MemberEntry getMemberEntry(Long entryId) {
        return memberEntryRepository.findById(entryId)
                .orElseThrow(() -> new BusinessException(ENTRY_NOT_FOUND));
    }

    public void deleteOrphanEntry(User entrantUser, Field hostField) {
        memberEntryRepository.deleteAllByEntrantUser(entrantUser);
        if (hostField.getCurrentSize() == hostField.getMaxSize()){
            memberEntryRepository.deleteAllByHostField(hostField);
        }
    }

}
