package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindAllFieldsDto {
    private Long id;

    private String name;

    private String profileImg;

    private FieldType fieldType;

    private int currentSize;

    private int maxSize;

    private SkillLevel skillLevel;

    private Period period;

    private Goal goal;
}
