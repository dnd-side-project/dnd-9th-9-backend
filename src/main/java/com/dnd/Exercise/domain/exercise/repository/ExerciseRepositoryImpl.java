package com.dnd.Exercise.domain.exercise.repository;

import static com.dnd.Exercise.domain.exercise.entity.QExercise.exercise;
import static com.dnd.Exercise.domain.user.entity.QUser.user;

import com.dnd.Exercise.domain.exercise.dto.response.RecentSportsDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.QFindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.RankCriterion;
import com.dnd.Exercise.domain.userField.dto.response.TopPlayerDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RankingDto> findTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date,
            List<Long> userIds) {

        NumberExpression<?> aggregateExpression = getAggregateExpression(rankCriterion);

        return queryFactory
                .select(Projections.constructor(RankingDto.class, user.id, user.profileImg, aggregateExpression))
                .from(exercise)
                .join(exercise.user,
                        user)
                .where(exercise.exerciseDate.eq(date)
                        .and(exercise.user.id.in(userIds)))
                .groupBy(user)
                .orderBy(aggregateExpression.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public TopPlayerDto findAccumulatedTopByDynamicCriteria(RankCriterion rankCriterion,
            LocalDate date, List<Long> userIds) {

        NumberExpression<?> aggregateExpression = getAggregateExpression(rankCriterion);

        return queryFactory
                .select(Projections.constructor(TopPlayerDto.class, exercise.user.name, aggregateExpression))
                .from(exercise)
                .join(exercise.user, user)
                .where(exercise.exerciseDate.between(date, LocalDate.now())
                        .and(exercise.user.id.in(userIds)))
                .groupBy(user)
                .orderBy(aggregateExpression.desc())
                .limit(1)
                .fetchFirst();
    }

    @Override
    public List<FindFieldRecordDto> findAllWithUser(LocalDate date, List<Long> userIds, Pageable pageable, Long leaderId) {

        BooleanExpression isLeader = getIsLeader(leaderId);

        return queryFactory
                .select(new QFindFieldRecordDto(exercise.id, user.id, user.profileImg, user.name,
                        isLeader, exercise.sports, exercise.recordingDateTime, exercise.durationMinute,
                        exercise.burnedCalorie, exercise.memoImg, exercise.memoContent,
                        exercise.isMemoPublic))
                .from(exercise)
                .join(exercise.user, user)
                .where(exercise.exerciseDate.eq(date)
                .and(exercise.user.id.in(userIds)))
                .orderBy(exercise.recordingDateTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression getIsLeader(Long leaderId){
        return user.id.eq(leaderId);
    }

    private NumberExpression<?> getAggregateExpression(RankCriterion criterion) {
        switch (criterion) {
            case EXERCISE_TIME:
                return exercise.durationMinute.sum();
            case RECORD_COUNT:
                return exercise.count().castToNum(Integer.class);
            default:
                throw new IllegalArgumentException("Invalid criterion: " + criterion);
        }
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






