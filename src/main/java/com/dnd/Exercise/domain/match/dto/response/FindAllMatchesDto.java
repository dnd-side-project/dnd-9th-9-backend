package com.dnd.Exercise.domain.match.dto.response;

import com.dnd.Exercise.domain.match.entity.Goal;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.match.entity.Period;
import com.dnd.Exercise.domain.match.entity.SkillLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindAllMatchesDto {

    private String name;

    private String profileImg;

    private MatchType matchType;

    private int memberCount;

    private int memberMaxCount;

    private SkillLevel skillLevel;

    private Period period;

    private Goal goal;
}
