package com.dnd.Exercise.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateNotificationAgreementReq {
    private Boolean isNotificationAgreed;
}
