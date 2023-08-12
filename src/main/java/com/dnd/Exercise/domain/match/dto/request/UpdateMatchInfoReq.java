package com.dnd.Exercise.domain.match.dto.request;

import com.dnd.Exercise.domain.match.entity.Goal;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.match.entity.Period;
import com.dnd.Exercise.domain.match.entity.SkillLevel;
import com.dnd.Exercise.domain.match.entity.Strength;
import lombok.Getter;

@Getter
public class UpdateMatchInfoReq {

    private Strength strength;

    private Goal goal;

    private int maxSize;

    private Period period;

    private MatchType matchType;

    private SkillLevel skillLevel;
}
