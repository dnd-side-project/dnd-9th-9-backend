package com.dnd.Exercise.domain.userField.dto.response;

import com.dnd.Exercise.domain.field.entity.SkillLevel;
import lombok.Data;

@Data
public class FindAllMembersRes {

    private Long id;

    private String name;

    private String profileImg;

    private SkillLevel skillLevel;

    private Boolean isLeader;
}
