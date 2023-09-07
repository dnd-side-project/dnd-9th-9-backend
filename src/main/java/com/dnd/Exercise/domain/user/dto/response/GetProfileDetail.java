package com.dnd.Exercise.domain.user.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.user.entity.Gender;
import com.dnd.Exercise.domain.user.entity.LoginType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class GetProfileDetail {
    private String uid;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String name;
    private String profileImg;

    private String age;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int height;
    private int weight;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;
    private int calorieGoal;

    private int teamworkRate;

    private Boolean isNotificationAgreed;
    private Boolean isAppleLinked;
}
