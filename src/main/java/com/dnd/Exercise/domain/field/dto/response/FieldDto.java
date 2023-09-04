package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.FieldStatus;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.field.entity.enums.Strength;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldDto {
    private Long id;

    private String name;

    private String description;

    private String profileImg;

    private String rule;

    private Strength strength;

    private Goal goal;

    private int maxSize;

    private int currentSize;

    private Period period;

    private LocalDate endDate;

    private FieldType fieldType;

    private SkillLevel skillLevel;

    private FieldStatus fieldStatus;

    private FieldRole fieldRole;
}
