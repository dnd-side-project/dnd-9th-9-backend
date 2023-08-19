package com.dnd.Exercise.domain.fieldEntry.dto.request;

import com.dnd.Exercise.domain.field.entity.TeamType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamFieldEntryReq {
    @NotNull
    private Long targetFieldId;

    private TeamType teamType;
}
