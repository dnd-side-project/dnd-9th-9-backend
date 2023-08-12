package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.Goal;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import com.dnd.Exercise.domain.field.entity.Strength;
import lombok.Getter;

@Getter
public class FindAllFieldsCond {

    private FieldType fieldType;

    private int memberCount;

    private SkillLevel skillLevel;

    private Strength strength;

    private Period period;

    private Goal goal;
}
