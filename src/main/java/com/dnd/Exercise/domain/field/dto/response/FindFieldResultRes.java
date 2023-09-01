package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.Badge.entity.BadgeDto;
import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.WinStatus;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindFieldResultRes {

    private Period period;

    private List<BadgeDto> badgeList;

    private WinStatus winStatus;

    private ElementWiseWinDto elementWiseWin;

    private FindFieldResultDto home;

    private FindFieldResultDto away;
}
