package com.dnd.Exercise.domain.auth.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class ChangePwReq {
    @ApiModelProperty(value = "10~11 자리 숫자의 휴대폰번호", required = true, example = "01012345678")
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "휴대폰 번호는 10~11자리의 숫자로만 입력 가능합니다.")
    private String phoneNum;

    @ApiModelProperty(value = "유저 아이디", required = true)
    @NotBlank
    private String uid;

    @ApiModelProperty(value = "새로 설정할 비밀번호", required = true)
    @NotBlank
    private String newPassword;

    @ApiModelProperty(value = "새로 설정할 비밀번호 확인용", required = true)
    @NotBlank
    private String confirmPassword;
}
