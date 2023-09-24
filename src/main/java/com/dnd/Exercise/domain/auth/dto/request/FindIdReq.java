package com.dnd.Exercise.domain.auth.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class FindIdReq {
    @ApiModelProperty(value = "10~11 자리 숫자의 휴대폰번호", required = true, example = "01012345678")
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "휴대폰 번호는 10~11자리의 숫자로만 입력 가능합니다.")
    private String phoneNum;

    @ApiModelProperty(value = "유저 이름", required = true, example = "정지민")
    @NotBlank
    private String name;
}
