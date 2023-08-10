package com.dnd.Exercise.domain.match.dto.request;

import com.dnd.Exercise.domain.match.entity.Goal;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.match.entity.Period;
import com.dnd.Exercise.domain.match.entity.SkillLevel;
import com.dnd.Exercise.domain.match.entity.Strength;
import lombok.Getter;

@Getter
public class FindAllMatchesCond {

    private MatchType matchType;

    private int memberCount;

    private SkillLevel skillLevel;

    private Strength strength;

    private Period period;

    private Goal goal;
}
