package com.dnd.Exercise.domain.field.dto.response;

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

    public void updateAssignedField(FindAllFieldsDto assignedFieldDto){
        this.assignedFieldDto = assignedFieldDto;
    }
}
