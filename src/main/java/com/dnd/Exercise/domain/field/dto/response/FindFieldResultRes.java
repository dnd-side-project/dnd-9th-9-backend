package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.Badge.entity.BadgeDto;
import com.dnd.Exercise.domain.field.entity.Period;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindFieldResultRes {

    private Period period;

    private List<BadgeDto> badgeList;

    private FindFieldResultDto home;

    private FindFieldResultDto away;
}
