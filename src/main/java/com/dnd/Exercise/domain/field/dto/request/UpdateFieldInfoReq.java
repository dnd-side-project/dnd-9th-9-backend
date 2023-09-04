package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.field.entity.enums.Strength;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class UpdateFieldInfoReq {
    @NotNull
    private Strength strength;

    @NotNull
    private Goal goal;

    @Range(min = 1, max = 10)
    @NotNull
    private int maxSize;

    @NotNull
    private Period period;

    @NotNull
    private FieldType fieldType;

    @NotNull
    private SkillLevel skillLevel;
}
