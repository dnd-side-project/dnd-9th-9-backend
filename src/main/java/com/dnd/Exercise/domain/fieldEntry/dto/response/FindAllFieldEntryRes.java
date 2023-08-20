package com.dnd.Exercise.domain.fieldEntry.dto.response;

import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindAllFieldEntryRes {

    private Long entryId;

    private Long matchId;

    private String name;

    private FieldType fieldType;

    private int memberCount;

    private int memberMaxCount;

    private SkillLevel skillLevel;

    private Period period;
}
