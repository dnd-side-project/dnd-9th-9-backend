package com.dnd.Exercise.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthCodeReq {
    private String code;
}
