package com.dnd.Exercise.domain.matchEntry.dto.response;

import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.match.entity.Period;
import com.dnd.Exercise.domain.match.entity.SkillLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindAllMatchEntryDto {
    private Long entryId;

    private Long matchId;

    private String name;

    private MatchType matchType;

    private int memberCount;

    private int memberMaxCount;

    private SkillLevel skillLevel;

    private Period period;
}
