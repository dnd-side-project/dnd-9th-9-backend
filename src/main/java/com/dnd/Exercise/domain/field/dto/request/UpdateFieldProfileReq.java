package com.dnd.Exercise.domain.field.dto.request;

import lombok.Getter;

@Getter
public class UpdateFieldProfileReq {
    private String name;

    private String profileImg;

    private String rule;

    private String description;
}
