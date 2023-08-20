package com.dnd.Exercise.domain.fieldEntry.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryRes;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface FieldEntryRepositoryCustom {
    List<FindAllTeamEntryRes> findAllTeamEntryByHostField(Field field, Pageable pageable);
}
