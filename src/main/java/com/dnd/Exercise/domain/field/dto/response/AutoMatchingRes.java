package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.Goal;
import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import com.dnd.Exercise.domain.field.entity.Strength;

public class AutoMatchingRes {

    private String name;

    private String profileImg;

    private Strength strength;

    private Goal goal;

    private Period period;

    private SkillLevel skillLevel;

    private int maxSize;

    private int currentSize;
}
