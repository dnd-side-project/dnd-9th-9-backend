package com.dnd.Exercise.domain.verification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SendCodeReq {
    @NotBlank
    private String phoneNum;
}
