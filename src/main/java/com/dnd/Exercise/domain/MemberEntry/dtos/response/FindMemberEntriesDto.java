package com.dnd.Exercise.domain.MemberEntry.dtos.response;

import com.dnd.Exercise.domain.MemberEntry.entity.MemberEntry;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindMemberEntriesDto {

    private Long entryId;

    private Long userId;

    private String name;

    private String profileImg;

    private SkillLevel skillLevel;

    public static FindMemberEntriesDto from(MemberEntry memberEntry){
        User entrantUser = memberEntry.getEntrantUser();
        return FindMemberEntriesDto.builder()
                .entryId(memberEntry.getId())
                .userId(entrantUser.getId())
                .name(entrantUser.getName())
                .profileImg(entrantUser.getProfileImg())
                .skillLevel(entrantUser.getSkillLevel())
                .build();
    }
}
