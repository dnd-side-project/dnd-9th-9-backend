package com.dnd.Exercise.domain.fieldEntry.dto.request;

import com.dnd.Exercise.domain.field.entity.FieldType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FieldEntryReq {

    private Long myFieldId;

    @NotNull
    private Long opponentFieldId;

    private FieldType fieldType;
}
