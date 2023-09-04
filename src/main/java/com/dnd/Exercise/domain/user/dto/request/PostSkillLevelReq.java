package com.dnd.Exercise.domain.user.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSkillLevelReq {
    private SkillLevel skillLevel;
}
