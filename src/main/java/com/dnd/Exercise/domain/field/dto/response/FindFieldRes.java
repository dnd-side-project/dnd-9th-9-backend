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
}
