package com.dnd.Exercise.domain.auth.dto.request;

import com.dnd.Exercise.domain.field.entity.SkillLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignUpReq {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String uid;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNum;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private SkillLevel skillLevel;
}
