package com.dnd.Exercise.domain.verification.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NaverSmsRes {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
}
