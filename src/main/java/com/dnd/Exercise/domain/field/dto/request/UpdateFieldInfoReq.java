package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.Goal;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import com.dnd.Exercise.domain.field.entity.Strength;
import lombok.Getter;

@Getter
public class UpdateFieldInfoReq {

    private Strength strength;

    private Goal goal;

    private int maxSize;

    private Period period;

    private FieldType fieldType;

    private SkillLevel skillLevel;
}
