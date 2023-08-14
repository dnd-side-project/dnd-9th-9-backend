package com.dnd.Exercise.domain.userField.dto.response;

import java.time.LocalDate;
import lombok.Data;

@Data
public class FindMyBattleStatusRes {

    private Long fieldId;

    private int daysLeft;

    private BattleStatusDto home;

    private BattleStatusDto away;
}
