package com.dnd.Exercise.domain.userField.dto.response;

import com.dnd.Exercise.domain.field.entity.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindMyBattleStatusRes {

    private Long fieldId;

    private Long daysLeft;

    private BattleStatusDto home;

    private BattleStatusDto away;

    public static FindMyBattleStatusRes from(BattleStatusDto home, BattleStatusDto away, Field myField){
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), myField.getEndDate());
        return FindMyBattleStatusRes.builder()
                .fieldId(myField.getId())
                .daysLeft(daysLeft)
                .home(home)
                .away(away)
                .build();
    }
}
