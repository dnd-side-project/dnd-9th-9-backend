package com.dnd.Exercise.domain.field.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindFieldRes {

    private FieldDto fieldDto;

    private FindAllFieldsDto assignedFieldDto;
}
