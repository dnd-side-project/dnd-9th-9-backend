package com.dnd.Exercise.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class OAuthLoginReq {
    @NotBlank
    private String token;
}
