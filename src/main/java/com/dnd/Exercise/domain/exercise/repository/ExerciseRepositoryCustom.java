package com.dnd.Exercise.domain.exercise.repository;

import com.dnd.Exercise.domain.exercise.dto.response.RecentSportsDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
import com.dnd.Exercise.domain.userField.dto.response.TopPlayerDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExerciseRepositoryCustom {

    List<RankingDto> findTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);

    TopPlayerDto findAccumulatedTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date, List<Long> userIds);

    Page<FindFieldRecordDto> findAllByUserAndDate(LocalDate date, List<Long> userIds, Pageable pageable, Long leaderId);

    void deleteUnexistingAppleWorkouts(List<String> existingAppleUids);

    List<RecentSportsDto> findDailyRecentSports(LocalDate date, Long userId);
}

