package com.dnd.Exercise.domain.fieldEntry.dto.request;

import com.dnd.Exercise.domain.field.entity.FieldType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteFieldEntryReq {
    private FieldType fieldType;

    private FieldDirection fieldDirection;
}
