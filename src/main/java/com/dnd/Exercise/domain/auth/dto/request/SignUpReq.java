package com.dnd.Exercise.domain.auth.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignUpReq {
    @ApiModelProperty(value = "아이디", required = true, example = "hana_bi")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String uid;

    @ApiModelProperty(value = "비밀번호", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @ApiModelProperty(value = "전화번호", required = true, example = "01012345678")
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNum;

    @ApiModelProperty(value = "이름", required = true , example = "정지민")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @ApiModelProperty(value = "운동레벨")
    private SkillLevel skillLevel;
}
