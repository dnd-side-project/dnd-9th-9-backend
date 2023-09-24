package com.dnd.Exercise.domain.verification.dto.request;

import com.dnd.Exercise.domain.verification.dto.VerifyingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.dnd.Exercise.global.common.Constants.PHONE_NUM_REGEXP;

@Getter
@NoArgsConstructor
public class VerifyReq {
    @ApiModelProperty(value = "10~11 자리 숫자의 휴대폰번호", required = true, example = "01012345678")
    @NotBlank
    @Pattern(regexp = PHONE_NUM_REGEXP, message = "휴대폰 번호는 10~11자리의 숫자로만 입력 가능합니다.")
    private String phoneNum;

    @ApiModelProperty(value = "6자리 숫자 인증번호", required = true, example = "001122")
    @NotBlank
    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리의 숫자로만 입력 가능합니다.")
    private String code;

    @ApiModelProperty(value = "인증 목적 (회원가입을 위한 인증 / 아이디찾기를 위한 인증 / 비밀번호찾기를 위한 인증)", required = true, example = "SIGN_UP / FIND_ID / FIND_PW")
    @NotNull
    private VerifyingType verifyingType;
}
