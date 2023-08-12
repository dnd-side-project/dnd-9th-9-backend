package com.dnd.Exercise.domain.fieldEntry.dto.response;

import com.dnd.Exercise.domain.field.entity.SkillLevel;
import lombok.Data;

@Data
public class FindAllTeamEntryDto {
    private Long entryId;

    private Long userId;

    private String name;

    private String profileImg;

    private SkillLevel skillLevel;
}
