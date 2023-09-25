package com.dnd.Exercise.domain.verification.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.dnd.Exercise.global.common.Constants.PHONE_NUM_REGEXP;

@Getter
@NoArgsConstructor
public class SignUpCodeReq {
    @ApiModelProperty(value = "10~11 자리 숫자의 휴대폰번호", required = true, example = "01012345678")
    @NotBlank
    @Pattern(regexp = PHONE_NUM_REGEXP, message = "휴대폰 번호는 10~11자리의 숫자로만 입력 가능합니다.")
    private String phoneNum;
}
