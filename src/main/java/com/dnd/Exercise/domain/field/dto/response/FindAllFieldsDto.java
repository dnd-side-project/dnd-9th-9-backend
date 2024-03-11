package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAllFieldsDto {
    private Long id;

    private String name;

    private String profileImg;

    private FieldType fieldType;

    private int currentSize;

    private int maxSize;

    private SkillLevel skillLevel;

    private Period period;

    private Goal goal;

    public static FindAllFieldsDto of(Field field){
        return FindAllFieldsDto.builder()
                .id(field.getId())
                .name(field.getName())
                .profileImg(field.getProfileImg())
                .fieldType(field.getFieldType())
                .currentSize(field.getCurrentSize())
                .maxSize(field.getMaxSize())
                .skillLevel(field.getSkillLevel())
                .period(field.getPeriod())
                .goal(field.getGoal())
                .build();
    }
}
