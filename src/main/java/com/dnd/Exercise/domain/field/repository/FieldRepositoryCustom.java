package com.dnd.Exercise.domain.field.repository;

import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
import java.time.LocalDate;
import java.util.List;

public interface FieldRepositoryCustom {
    List<Field> findAllFieldsWithFilter(FindAllFieldsCond findAllFieldsCond);

    Long countAllFieldsWithFilter(FindAllFieldsCond findAllFieldsCond);

    List<RankingDto> findTopByActivityCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);

    List<RankingDto> findTopByExerciseCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);
}
