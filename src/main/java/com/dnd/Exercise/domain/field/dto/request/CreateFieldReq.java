package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.Goal;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import com.dnd.Exercise.domain.field.entity.Strength;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;


@Getter
public class CreateFieldReq {

    @NotBlank
    private String name;

    private String profileImg;

    @NotNull
    private Strength strength;

    @NotNull
    private Goal goal;

    private String rule;

    @Range(min = 1, max = 10)
    @NotNull
    private int maxSize;

    @NotNull
    private Period period;

    private String description;

    @NotNull
    private FieldType fieldType;

    @NotNull
    private SkillLevel skillLevel;

    public Field toEntity(Long leaderId){
        return Field.builder()
                .name(name)
                .profileImg(profileImg)
                .strength(strength)
                .goal(goal)
                .rule(rule)
                .maxSize(maxSize)
                .period(period)
                .description(description)
                .leaderId(leaderId)
                .fieldType(fieldType)
                .skillLevel(skillLevel)
                .build();
    }
}

