package com.dnd.Exercise.domain.verification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendCodeReq {
    private String phoneNum;
}
