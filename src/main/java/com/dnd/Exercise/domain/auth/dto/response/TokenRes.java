package com.dnd.Exercise.domain.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRes {

    private String accessToken;

    private String refreshToken;
}
