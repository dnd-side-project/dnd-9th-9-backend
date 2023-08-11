package com.dnd.Exercise.domain.userMatch.dto.response;

import com.dnd.Exercise.domain.match.entity.SkillLevel;
import lombok.Data;

@Data
public class FindAllMembersRes {

    private Long id;

    private String name;

    private String profileImg;

    private SkillLevel skillLevel;
}
