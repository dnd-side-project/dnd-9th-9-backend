package com.dnd.Exercise.domain.verification.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class VerifySignUpReq {
    @ApiModelProperty(value = "10~11 자리 숫자의 휴대폰번호", required = true, example = "01012345678")
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "휴대폰 번호는 10~11자리의 숫자로만 입력 가능합니다.")
    private String phoneNum;

    @ApiModelProperty(value = "6자리 숫자 인증번호", required = true, example = "001122")
    @NotBlank
    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리의 숫자로만 입력 가능합니다.")
    private String code;
}
