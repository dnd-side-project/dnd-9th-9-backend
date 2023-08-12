package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.Goal;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.Strength;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class CreateFieldReq {

    @NotBlank
    private String name;

    private String profile_img;

    @NotNull
    private Strength strength;

    @NotNull
    private Goal goal;

    private String rule;

    @NotNull
    private int maxSize;

    @NotNull
    private Period period;

    private String description;

    @NotNull
    private FieldType fieldType;
}

