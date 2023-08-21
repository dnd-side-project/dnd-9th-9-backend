package com.dnd.Exercise.domain.field.entity;

public enum TeamType {
    TEAM, TEAM_BATTLE;

    public FieldType toFieldType() {
        if (this == TeamType.TEAM_BATTLE) {
            return FieldType.TEAM_BATTLE;
        } else if (this == TeamType.TEAM) {
            return FieldType.TEAM;
        }
        return null;
    }
}
