package com.dnd.Exercise.domain.field.entity.enums;

public enum BattleType {
    DUEL, TEAM_BATTLE;

    public FieldType toFieldType() {
        if (this == BattleType.TEAM_BATTLE)
            return FieldType.TEAM_BATTLE;
        else if (this == BattleType.DUEL)
            return FieldType.DUEL;
        return null;
    }
}
