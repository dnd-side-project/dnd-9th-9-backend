package com.dnd.Exercise.domain.fieldEntry.dto.response;

import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllBattleEntryRes {

    private Long entryId;

    private Long fieldId;

    private String name;

    private FieldType fieldType;

    private int memberCount;

    private int memberMaxCount;

    private SkillLevel skillLevel;

    private Period period;
}
