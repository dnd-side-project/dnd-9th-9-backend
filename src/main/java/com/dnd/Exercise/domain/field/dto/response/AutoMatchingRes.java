package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.field.entity.enums.Strength;
import lombok.Data;

@Data
public class AutoMatchingRes {
    private Long id;

    private String name;

    private String profileImg;

    private Strength strength;

    private Goal goal;

    private Period period;

    private SkillLevel skillLevel;

    private int maxSize;

    private int currentSize;
}
