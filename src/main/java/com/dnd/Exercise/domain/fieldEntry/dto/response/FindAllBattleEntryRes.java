package com.dnd.Exercise.domain.fieldEntry.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllBattleEntryRes {

    private Long entryId;

    private Long fieldId;

    private String name;

    private FieldType fieldType;

    private int currentSize;

    private int maxSize;

    private SkillLevel skillLevel;

    private Period period;
}
