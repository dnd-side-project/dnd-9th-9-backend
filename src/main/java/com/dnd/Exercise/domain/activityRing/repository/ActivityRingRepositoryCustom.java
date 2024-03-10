package com.dnd.Exercise.domain.activityRing.repository;

import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
import java.time.LocalDate;
import java.util.List;

public interface ActivityRingRepositoryCustom {
    List<RankingDto> findTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);
}
