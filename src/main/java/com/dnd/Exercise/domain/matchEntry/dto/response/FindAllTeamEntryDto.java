package com.dnd.Exercise.domain.matchEntry.dto.response;

import com.dnd.Exercise.domain.match.entity.SkillLevel;
import lombok.Data;

@Data
public class FindAllTeamEntryDto {
    private Long entryId;

    private Long userId;

    private String name;

    private String profileImg;

    private SkillLevel skillLevel;
}
