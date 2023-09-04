package com.dnd.Exercise.domain.fieldEntry.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAllTeamEntryDto {

    private Long entryId;

    private Long userId;

    private String name;

    private String profileImg;

    private SkillLevel skillLevel;
}
