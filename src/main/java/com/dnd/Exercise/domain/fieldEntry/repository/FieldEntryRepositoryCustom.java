package com.dnd.Exercise.domain.fieldEntry.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryDto;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FieldEntryRepositoryCustom {
    Page<FindAllTeamEntryDto> findAllTeamEntryByHostField(Field field, Pageable pageable);

    Page<FindAllBattleEntryDto> findAllBattleByField(Field field, FieldDirection fieldDirection, Pageable pageable);
}
