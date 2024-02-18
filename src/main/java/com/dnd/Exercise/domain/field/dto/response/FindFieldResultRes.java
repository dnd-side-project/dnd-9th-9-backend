package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.Badge.entity.BadgeDto;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindFieldResultRes {

    private Period period;

    private List<BadgeDto> badgeList;

    private WinStatus winStatus;

    private ElementWiseWin elementWiseWin;

    private FindFieldResultDto home;

    private FindFieldResultDto away;

    private int teamworkRate;

    private LocalDate startDate;

    private LocalDate endDate;

    public static FindFieldResultRes from(FindFieldResultDto home, Field field, int teamworkRate){
        return FindFieldResultRes.builder()
                .period(field.getPeriod())
                .startDate(field.getStartDate())
                .endDate(field.getEndDate())
                .home(home)
                .teamworkRate(teamworkRate)
                .build();
    }

    public void updateIfNotTeam(
            FindFieldResultDto away,
            ElementWiseWin elementWiseWin,
            WinStatus winStatus){
        this.away = away;
        this.elementWiseWin = elementWiseWin;
        this.winStatus = winStatus;
    }
}
