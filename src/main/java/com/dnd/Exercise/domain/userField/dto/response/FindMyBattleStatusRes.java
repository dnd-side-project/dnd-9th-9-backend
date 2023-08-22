package com.dnd.Exercise.domain.userField.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindMyBattleStatusRes {

    private Long fieldId;

    private Long daysLeft;

    private BattleStatusDto home;

    private BattleStatusDto away;
}
