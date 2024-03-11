package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindFieldRes {

    private FieldDto fieldDto;

    private FindAllFieldsDto assignedFieldDto;

    public static FindFieldRes from(FieldDto fieldDto){
        return FindFieldRes.builder()
                .fieldDto(fieldDto)
                .build();
    }

    public void updateAssignedField(Field field, Boolean isMember) {
        Field opponentField = field.getOpponent();
        if (isMember && opponentField != null){
            FindAllFieldsDto assignedFieldDto = FindAllFieldsDto.of(opponentField);
            this.assignedFieldDto = assignedFieldDto;
        }
    }
}
