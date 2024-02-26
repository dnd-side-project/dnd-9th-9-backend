package com.dnd.Exercise.domain.activityRing.repository;

import static com.dnd.Exercise.domain.activityRing.entity.QActivityRing.activityRing;
import static com.dnd.Exercise.domain.user.entity.QUser.user;

import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ActivityRingRepositoryImpl implements ActivityRingRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RankingDto> findTopByDynamicCriteria(RankCriterion rankCriterion, LocalDate date,
            List<Long> userIds) {

        NumberExpression<?> aggregateExpression = getAggregateExpression(rankCriterion);

        return queryFactory
                .select(Projections.constructor(RankingDto.class, user.id, user.profileImg, aggregateExpression))
                .from(activityRing)
                .join(activityRing.user,
                        user)
                .where(activityRing.date.eq(date)
                        .and(activityRing.user.id.in(userIds)))
                .groupBy(user)
                .orderBy(aggregateExpression.desc())
                .limit(3)
                .fetch();
    }

    private NumberExpression<?> getAggregateExpression(RankCriterion criterion) {
        switch (criterion) {
            case BURNED_CALORIE:
                return activityRing.burnedCalorie.sum();
            case GOAL_ACHIEVED:
                return activityRing.isGoalAchieved.castToNum(Integer.class).sum();
            default:
                throw new IllegalArgumentException("Invalid criterion: " + criterion);
        }
    }
}
