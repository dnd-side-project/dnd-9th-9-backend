package com.dnd.Exercise.domain.userField.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindAllMembersRes {

    private Long id;

    private String name;

    private String profileImg;

    private SkillLevel skillLevel;

    private Boolean isLeader;

    public static FindAllMembersRes from(User user, Long leaderId){
        return FindAllMembersRes.builder()
                .id(user.getId())
                .name(user.getName())
                .profileImg(user.getProfileImg())
                .skillLevel(user.getSkillLevel())
                .isLeader(leaderId.equals(user.getId()))
                .build();
    }
}
