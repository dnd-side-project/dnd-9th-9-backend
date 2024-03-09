package com.dnd.Exercise.domain.field.dto.request;

import static com.dnd.Exercise.domain.field.entity.enums.FieldType.DUEL;
import static com.dnd.Exercise.global.error.dto.ErrorCode.DUEL_MAX_ONE;

import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.field.entity.enums.Strength;
import com.dnd.Exercise.global.error.exception.BusinessException;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class UpdateFieldInfoReq {
    @NotNull
    private Strength strength;

    @NotNull
    private Goal goal;

    @Range(min = 1, max = 10)
    @NotNull
    private int maxSize;

    @NotNull
    private Period period;

    @NotNull
    private FieldType fieldType;

    @NotNull
    private SkillLevel skillLevel;

    public void validateDuelMaxSize() {
        if (DUEL.equals(this.fieldType) && this.maxSize != 1) {
            throw new BusinessException(DUEL_MAX_ONE);
        }
    }
}
