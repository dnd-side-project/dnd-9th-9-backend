package com.dnd.Exercise.domain.match.dto.response;

import com.dnd.Exercise.domain.match.entity.Goal;
import com.dnd.Exercise.domain.match.entity.Period;
import com.dnd.Exercise.domain.match.entity.SkillLevel;
import com.dnd.Exercise.domain.match.entity.Strength;

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
