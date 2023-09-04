package com.dnd.Exercise.domain.fieldEntry.service;

import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.fieldEntry.dto.request.BattleFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection;
import com.dnd.Exercise.domain.fieldEntry.dto.request.TeamFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryRes;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryRes;
import com.dnd.Exercise.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface FieldEntryService {

    void createTeamFieldEntry(User user, TeamFieldEntryReq fieldEntryReq);

    void createBattleFieldEntry(User user, BattleFieldEntryReq fieldEntryReq);

    void deleteFieldEntry(User user, Long entryId);

    void acceptFieldEntry(User user, Long entryId);

    List<FindAllTeamEntryRes> findAllTeamEntries(User user, Long fieldId, Pageable pageable);

    List<FindAllBattleEntryRes> findAllBattleEntriesByDirection(User user, Long fieldId, FieldDirection fieldDirection, Pageable pageable);

    List<FindAllBattleEntryRes> findAllBattleEntriesByType(User user, FieldType fieldType, Pageable pageable);
}
