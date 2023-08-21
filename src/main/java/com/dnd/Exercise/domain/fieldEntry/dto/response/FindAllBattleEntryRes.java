package com.dnd.Exercise.domain.fieldEntry.dto.response;

import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
