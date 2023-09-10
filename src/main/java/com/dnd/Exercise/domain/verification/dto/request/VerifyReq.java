package com.dnd.Exercise.domain.verification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class VerifyReq {
    @NotBlank
    private String phoneNum;

    @NotBlank
    private String code;
}
