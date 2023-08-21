package com.dnd.Exercise.domain.fieldEntry.dto.request;

import com.dnd.Exercise.domain.field.entity.BattleType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BattleFieldEntryReq {
    @NotNull
    private Long targetFieldId;

    private BattleType battleType;
}
