package com.dnd.Exercise.domain.fieldEntry.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BattleFieldEntryReq {
    @NotNull
    private Long targetFieldId;

    @NotNull
    private BattleType battleType;
}
