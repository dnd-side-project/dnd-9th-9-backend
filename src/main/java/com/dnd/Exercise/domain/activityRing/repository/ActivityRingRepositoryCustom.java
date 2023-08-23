package com.dnd.Exercise.domain.activityRing.repository;

import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.RankCriterion;
import com.dnd.Exercise.domain.userField.dto.response.TopPlayerDto;
import java.time.LocalDate;
import java.util.List;

public interface ActivityRingRepositoryCustom {
    List<RankingDto> findTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);

    TopPlayerDto findAccumulatedTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);
}
