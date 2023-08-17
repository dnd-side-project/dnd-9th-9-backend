package com.dnd.Exercise.domain.exercise.repository;

import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.RankCriterion;
import com.querydsl.core.Tuple;
import java.time.LocalDate;
import java.util.List;

public interface ExerciseRepositoryCustom {

    List<RankingDto> findTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);
}
