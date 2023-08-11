package com.dnd.Exercise.domain.match.dto.response;

import com.dnd.Exercise.domain.match.entity.Goal;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.match.entity.Period;
import com.dnd.Exercise.domain.match.entity.SkillLevel;
import com.dnd.Exercise.domain.match.entity.Strength;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchDto {
    private Long id;

    private String name;

    private String description;

    private String profileImg;

    private String rule;

    private Strength strength;

    private Goal goal;

    private int maxSize;

    private int currentSize;

    private Period period;

    private LocalDate endDate;

    private MatchType matchType;

    private SkillLevel skillLevel;
}
