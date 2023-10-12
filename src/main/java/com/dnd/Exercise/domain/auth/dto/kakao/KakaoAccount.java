package com.dnd.Exercise.domain.auth.dto.kakao;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoAccount {
    private Profile profile;
    private String email;
}
