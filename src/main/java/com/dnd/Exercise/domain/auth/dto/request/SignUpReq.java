package com.dnd.Exercise.domain.auth.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.dnd.Exercise.global.common.Constants.ID_REGEXP;
import static com.dnd.Exercise.global.common.Constants.PW_REGEXP;
import static com.dnd.Exercise.global.common.Constants.PHONE_NUM_REGEXP;

@Getter
@NoArgsConstructor
public class SignUpReq {
    @ApiModelProperty(value = "아이디", required = true, example = "hana_bi")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = ID_REGEXP, message = "아이디는 6~15 자리의 영문 & 숫자 조합으로만 입력 가능합니다.")
    private String uid;

    @ApiModelProperty(value = "비밀번호", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = PW_REGEXP, message = "비밀번호는 8~16 자리의 영문 & 숫자 조합으로만 입력 가능합니다.")
    private String password;

    @ApiModelProperty(value = "10~11 자리 숫자의 휴대폰번호", required = true, example = "01012345678")
    @NotBlank
    @Pattern(regexp = PHONE_NUM_REGEXP, message = "휴대폰 번호는 10~11자리의 숫자로만 입력 가능합니다.")
    private String phoneNum;

    @ApiModelProperty(value = "이름", required = true , example = "정지민")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @ApiModelProperty(value = "운동레벨")
    private SkillLevel skillLevel;
}
