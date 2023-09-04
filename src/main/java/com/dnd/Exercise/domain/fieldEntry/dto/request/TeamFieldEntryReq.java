package com.dnd.Exercise.domain.fieldEntry.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.TeamType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamFieldEntryReq {
    @NotNull
    private Long targetFieldId;

    @NotNull
    private TeamType teamType;
}
