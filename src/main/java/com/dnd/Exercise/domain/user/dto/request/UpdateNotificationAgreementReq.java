package com.dnd.Exercise.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateNotificationAgreementReq {
    @NotNull
    private Boolean isNotificationAgreed;
}
