package com.dnd.Exercise.domain.exercise.repository;

import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.RankCriterion;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ExerciseRepositoryCustom {

    List<RankingDto> findTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);

    List<FindFieldRecordDto> findAllWithUser(LocalDate date, List<Long> userIds, Pageable pageable, Long leaderId);
}
