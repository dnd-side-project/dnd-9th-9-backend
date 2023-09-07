package com.dnd.Exercise.domain.user.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostSkillLevelReq {
    @NotNull
    private SkillLevel skillLevel;
}
