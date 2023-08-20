package com.dnd.Exercise.domain.fieldEntry.dto.response;

import com.dnd.Exercise.domain.field.entity.SkillLevel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAllTeamEntryRes {

    private Long entryId;

    private Long userId;

    private String name;

    private String profileImg;

    private SkillLevel skillLevel;
}
