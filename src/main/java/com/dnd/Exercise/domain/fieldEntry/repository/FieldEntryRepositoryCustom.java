package com.dnd.Exercise.domain.fieldEntry.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryRes;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryRes;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface FieldEntryRepositoryCustom {
    List<FindAllTeamEntryRes> findAllTeamEntryByHostField(Field field, Pageable pageable);

    List<FindAllBattleEntryRes> findAllBattleByField(Field field, FieldDirection fieldDirection, Pageable pageable);
}
