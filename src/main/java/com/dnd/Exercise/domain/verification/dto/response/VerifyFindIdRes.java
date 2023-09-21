package com.dnd.Exercise.domain.verification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyFindIdRes {
    @Builder.Default
    private String message = "인증되었습니다.";

    private List<String> uids;
}
