package com.dnd.Exercise.domain.field.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateFieldProfileReq {
    @NotBlank
    private String name;

    private String profileImg;

    private String rule;

    private String description;
}
