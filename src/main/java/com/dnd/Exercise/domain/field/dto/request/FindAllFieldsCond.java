package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.Goal;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import com.dnd.Exercise.domain.field.entity.Strength;
import java.util.List;
import lombok.Getter;

@Getter
public class FindAllFieldsCond {

    private FieldType fieldType;

    private Integer memberCount;

    private List<SkillLevel> skillLevel;

    private List<Strength> strength;

    private List<Period> period;

    private List<Goal> goal;
}
