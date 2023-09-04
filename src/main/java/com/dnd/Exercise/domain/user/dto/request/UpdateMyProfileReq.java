package com.dnd.Exercise.domain.user.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMyProfileReq {
    private String name;

    private String profileImg;

    private int age;

    private int height;

    private int weight;

    private SkillLevel skillLevel;
}
