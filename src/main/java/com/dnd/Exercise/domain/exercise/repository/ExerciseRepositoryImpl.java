package com.dnd.Exercise.domain.exercise.repository;

import static com.dnd.Exercise.domain.exercise.entity.QExercise.exercise;
import static com.dnd.Exercise.domain.field.entity.QField.field;
import static com.dnd.Exercise.domain.user.entity.QUser.user;

import com.dnd.Exercise.domain.exercise.dto.response.RecentSportsDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.QFindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
import com.dnd.Exercise.domain.userField.dto.response.TopPlayerDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FindFieldRecordDto> findAllByUserAndDate(LocalDate date, List<Long> userIds, Pageable pageable, Long leaderId) {

        BooleanExpression isLeader = getIsLeader(leaderId);

        JPAQuery<Long> countQuery = queryFactory
                .select(exercise.count())
                .from(exercise)
                .join(exercise.user, user)
                .where(exercise.exerciseDate.eq(date).and(exercise.user.id.in(userIds)));

        List<FindFieldRecordDto> results = queryFactory
                .select(new QFindFieldRecordDto(exercise, user, isLeader))
                .from(exercise)
                .join(exercise.user, user)
                .where(exercise.exerciseDate.eq(date).and(exercise.user.id.in(userIds)))
                .orderBy(exercise.recordingDateTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression getIsLeader(Long leaderId){
        return user.id.eq(leaderId);
    }

    @Override
    public void deleteUnexistingAppleWorkouts(List<String> existingAppleUids) {
       queryFactory
                .delete(exercise)
                .where(exercise.appleUid.isNotNull(),
                        exercise.appleUid.notIn(existingAppleUids))
                .execute();
    }

    @Override
    public List<RecentSportsDto> findDailyRecentSports(LocalDate date, Long userId) {
        return queryFactory
                .select(Projections.fields(RecentSportsDto.class,
                        exercise.sports.as("sports"),
                        exercise.durationMinute.sum().as("exerciseMinute"),
                        exercise.burnedCalorie.sum().as("burnedCalorie")
                        ))
                .from(exercise)
                .groupBy(exercise.sports)
                .orderBy(exercise.durationMinute.sum().desc(), exercise.burnedCalorie.sum().desc())
                .limit(4)
                .fetch();
    }
}






