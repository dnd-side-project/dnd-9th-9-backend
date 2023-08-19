package com.dnd.Exercise.domain.field.entity;

public enum FieldType {
    DUEL, TEAM_BATTLE, TEAM;

    public TeamType toTeamType() {
        if (this == FieldType.TEAM_BATTLE) {
            return TeamType.TEAM_BATTLE;
        } else if (this == FieldType.TEAM) {
            return TeamType.TEAM;
        }
        return null;
    }
}
