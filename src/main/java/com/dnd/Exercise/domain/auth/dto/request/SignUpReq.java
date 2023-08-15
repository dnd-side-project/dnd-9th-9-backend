package com.dnd.Exercise.domain.auth.dto.request;

import com.dnd.Exercise.domain.field.entity.SkillLevel;
import com.dnd.Exercise.domain.user.entity.LoginType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpReq {

    private String uid;

    private String password;

    private String phoneNum;

    private String name;

    private SkillLevel skillLevel;
}
