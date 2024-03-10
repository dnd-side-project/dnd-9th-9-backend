package com.dnd.Exercise.domain.BattleEntry.event;

import com.dnd.Exercise.domain.field.entity.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcceptBattleEvent {
    private Field entrantField;
    private Field hostField;

    public static AcceptBattleEvent from(Field entrantField, Field hostField){
        return new AcceptBattleEvent(entrantField, hostField);
    }
}
