package com.dnd.Exercise.domain.MemberEntry.service;

import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.TEAM_ACCEPT;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ALREADY_APPLY;
import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static com.dnd.Exercise.global.error.dto.ErrorCode.ENTRY_NOT_FOUND;

import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesDto;
import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesRes;
import com.dnd.Exercise.domain.MemberEntry.dtos.response.FindMemberEntriesDto;
import com.dnd.Exercise.domain.MemberEntry.dtos.response.FindMemberEntriesRes;
import com.dnd.Exercise.domain.MemberEntry.entity.MemberEntry;
import com.dnd.Exercise.domain.MemberEntry.repository.MemberEntryRepository;
import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberEntryServiceImpl implements MemberEntryService{

    private final FieldBusiness fieldBusiness;
    private final MemberEntryRepository memberEntryRepository;
    private final UserFieldRepository userFieldRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void createMemberEntry(User user, Long fieldId) {
        Field hostField = fieldBusiness.getField(fieldId);
        checkCreateMemberEntryValidity(user, hostField);

        MemberEntry memberEntry = MemberEntry.from(user, hostField);
        memberEntryRepository.save(memberEntry);
    }

    @Override
    @Transactional
    public void cancelMemberEntry(User user, Long entryId) {
        MemberEntry memberEntry = getMemberEntry(entryId);
        checkCancelMemberEntryValidity(user, memberEntry);

        memberEntryRepository.deleteById(entryId);
    }

    @Override
    @Transactional
    public void acceptMemberEntry(User user, Long entryId) {
        MemberEntry memberEntry = getMemberEntry(entryId);
        User entrantUser = memberEntry.getEntrantUser();
        Field hostField = memberEntry.getHostField();

        checkAcceptMemberEntryValidity(user, hostField);

        hostField.addMember();
        userFieldRepository.save(UserField.from(entrantUser, hostField));
        deleteOrphanEntry(entrantUser, hostField);

        notificationService.sendUserNotification(TEAM_ACCEPT, hostField, entrantUser);
        notificationService.sendFieldNotification(TEAM_ACCEPT, hostField, entrantUser.getName());
    }

    @Override
    public FindMemberEntriesRes findReceivedMemberEntries(User user, Long fieldId, Pageable pageable) {
        Field field = fieldBusiness.getField(fieldId);
        fieldBusiness.validateIsMember(user, field);

        Page<MemberEntry> memberEntryPage = memberEntryRepository.findAllByHostField(field, pageable);
        Page<FindMemberEntriesDto> memberEntriesDtoPage = memberEntryPage.map(FindMemberEntriesDto::from);

        return FindMemberEntriesRes.from(memberEntriesDtoPage, pageable);
    }

    @Override
    public FindBattleEntriesRes findMySentMemberEntries(User user, Pageable pageable) {
        Page<MemberEntry> memberEntryPage = memberEntryRepository.findAllByEntrantUser(user, pageable);
        Page<FindBattleEntriesDto> battleEntriesDtoPage = memberEntryPage.map(FindBattleEntriesDto::toSentEntryDto);

        return FindBattleEntriesRes.from(battleEntriesDtoPage, pageable);
    }

    private void deleteOrphanEntry(User entrantUser, Field hostField) {
        memberEntryRepository.deleteAllByEntrantUser(entrantUser);
        if (hostField.getCurrentSize() == hostField.getMaxSize()){
            memberEntryRepository.deleteAllByHostField(hostField);
        }
    }

    private void checkAcceptMemberEntryValidity(User user, Field hostField) {
        hostField.validateIsLeader(user.getId());
        hostField.validateIsNotFull();
    }


    private void checkCreateMemberEntryValidity(User user, Field hostField) {
        FieldType fieldType = hostField.getFieldType();

        hostField.validateHaveOpponent();
        hostField.validateIsNotFull();
        validateDuplicateTeamApply(user, hostField);
        fieldBusiness.validateNotHavingField(user, fieldType);
    }

    private void validateDuplicateTeamApply(User user, Field hostField) {
        if(memberEntryRepository.existsByEntrantUserAndHostField(user, hostField)){
            throw new BusinessException(ALREADY_APPLY);
        }
    }

    private MemberEntry getMemberEntry(Long entryId) {
        return memberEntryRepository.findById(entryId)
                .orElseThrow(() -> new BusinessException(ENTRY_NOT_FOUND));
    }

    private void checkCancelMemberEntryValidity(User user, MemberEntry memberEntry) {
        Long entrantUserId = memberEntry.getEntrantUser().getId();
        Long userId = user.getId();
        if(!entrantUserId.equals(userId))
            throw new BusinessException(BAD_REQUEST);
    }
}
