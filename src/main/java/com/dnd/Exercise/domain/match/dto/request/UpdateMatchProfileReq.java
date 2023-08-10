package com.dnd.Exercise.domain.match.dto.request;

import lombok.Getter;

@Getter
public class UpdateMatchProfileReq {
    private String name;

    private String profileImg;

    private String rule;

    private String description;
}
