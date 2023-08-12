package com.dnd.Exercise.domain.fieldEntry.dto.response;

import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindAllFieldEntryDto {
    private Long entryId;

    private Long matchId;

    private String name;

    private FieldType fieldType;

    private int memberCount;

    private int memberMaxCount;

    private SkillLevel skillLevel;

    private Period period;
}
