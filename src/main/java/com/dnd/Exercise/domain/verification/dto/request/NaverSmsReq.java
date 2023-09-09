package com.dnd.Exercise.domain.verification.dto.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NaverSmsReq {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<MessageDto> messages;
}
