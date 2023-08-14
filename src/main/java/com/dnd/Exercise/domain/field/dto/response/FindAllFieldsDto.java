package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.Goal;
import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
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
