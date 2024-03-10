package com.dnd.Exercise.domain.MemberEntry.service;

import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesDto;
import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesRes;
import com.dnd.Exercise.domain.MemberEntry.business.MemberEntryBusiness;
import com.dnd.Exercise.domain.MemberEntry.dtos.response.FindMemberEntriesDto;
import com.dnd.Exercise.domain.MemberEntry.dtos.response.FindMemberEntriesRes;
import com.dnd.Exercise.domain.MemberEntry.entity.MemberEntry;
import com.dnd.Exercise.domain.MemberEntry.event.AcceptMemberEvent;
import com.dnd.Exercise.domain.MemberEntry.repository.MemberEntryRepository;
import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final MemberEntryBusiness memberEntryBusiness;
    private final ApplicationEventPublisher eventPublisher;

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
        MemberEntry memberEntry = memberEntryBusiness.getMemberEntry(entryId);
        memberEntry.validateMyEntry(user);

        memberEntryRepository.deleteById(entryId);
    }

    @Override
    @Transactional
    public void acceptMemberEntry(User user, Long entryId) {
        MemberEntry memberEntry = memberEntryBusiness.getMemberEntry(entryId);
        User entrantUser = memberEntry.getEntrantUser();
        Field hostField = memberEntry.getHostField();

        checkAcceptMemberEntryValidity(user, hostField);
        hostField.addMember();

        eventPublisher.publishEvent(AcceptMemberEvent.from(entrantUser, hostField));
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

    private void checkAcceptMemberEntryValidity(User user, Field hostField) {
        hostField.validateIsLeader(user.getId());
        hostField.validateIsNotFull();
    }


    private void checkCreateMemberEntryValidity(User user, Field hostField) {
        FieldType fieldType = hostField.getFieldType();

        hostField.validateHaveOpponent();
        hostField.validateIsNotFull();
        memberEntryBusiness.validateDuplicateTeamApply(user, hostField);
        fieldBusiness.validateNotHavingField(user, fieldType);
    }
}
