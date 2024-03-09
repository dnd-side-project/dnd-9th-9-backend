package com.dnd.Exercise.domain.field.repository;

import static com.dnd.Exercise.domain.activityRing.entity.QActivityRing.activityRing;
import static com.dnd.Exercise.domain.exercise.entity.QExercise.exercise;
import static com.dnd.Exercise.domain.field.entity.QField.field;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.RECRUITING;
import static com.dnd.Exercise.domain.user.entity.QUser.user;

import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FieldRepositoryImpl implements FieldRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Field> findAllFieldsWithFilter(FindAllFieldsCond findAllFieldsCond) {
        BooleanBuilder whereClause = filterBuilder(findAllFieldsCond);
        whereClause.and(ltFieldId(findAllFieldsCond.getFieldId()));

        return queryFactory
                .select(field)
                .from(field)
                .where(whereClause)
                .orderBy(field.id.asc())
                .limit(findAllFieldsCond.getSize())
                .fetch();
    }


    @Override
    public Long countAllFieldsWithFilter(FindAllFieldsCond findAllFieldsCond) {
        BooleanBuilder whereClause = filterBuilder(findAllFieldsCond);;

        return queryFactory
                .select(field.count())
                .from(field)
                .where(whereClause)
                .fetchOne();
    }

    @Override
    public List<RankingDto> findTopByActivityCriteria(
            RankCriterion rankCriterion, LocalDate date, List<Long> userIds) {

        NumberExpression<?> aggregateExpression = getAggregateExpression(rankCriterion);

        return queryFactory
                .select(Projections.constructor(RankingDto.class, user.id, user.profileImg, aggregateExpression))
                .from(activityRing)
                .join(activityRing.user, user)
                .where(activityRing.date.eq(date).and(activityRing.user.id.in(userIds)))
                .groupBy(user)
                .orderBy(aggregateExpression.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<RankingDto> findTopByExerciseCriteria(
            RankCriterion rankCriterion, LocalDate date, List<Long> userIds) {

        NumberExpression<?> aggregateExpression = getAggregateExpression(rankCriterion);

        return queryFactory
                .select(Projections.constructor(RankingDto.class, user.id, user.profileImg, aggregateExpression))
                .from(exercise)
                .join(exercise.user, user)
                .where(exercise.exerciseDate.eq(date).and(exercise.user.id.in(userIds)))
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
            case EXERCISE_TIME:
                return exercise.durationMinute.sum();
            case RECORD_COUNT:
                return exercise.count().castToNum(Integer.class);
            default:
                throw new IllegalArgumentException("Invalid criterion: " + criterion);
        }
    }


    private BooleanBuilder filterBuilder(FindAllFieldsCond findAllFieldsCond){
        BooleanBuilder whereClause = new BooleanBuilder();

        whereClause.and(evaluateEq(field.fieldType, findAllFieldsCond.getFieldType()))
                .and(evaluateEq(field.maxSize, findAllFieldsCond.getMemberCount()))
                .and(evaluateIn(field.skillLevel, findAllFieldsCond.getSkillLevel()))
                .and(evaluateIn(field.strength, findAllFieldsCond.getStrength()))
                .and(evaluateIn(field.period, findAllFieldsCond.getPeriod()))
                .and(evaluateIn(field.goal, findAllFieldsCond.getGoal()))
                .and(evaluateLike(field.name, findAllFieldsCond.getKeyword()))
                .and(field.fieldStatus.eq(RECRUITING));

        return whereClause;
    }

    public static <T> BooleanExpression evaluateEq(SimpleExpression<T> path, T value) {
        return value == null ? null : path.eq(value);
    }

    public static <T> BooleanExpression evaluateIn(SimpleExpression<T> path, Collection<T> values) {
        return values == null || values.isEmpty() ? null : path.in(values);
    }

    public static BooleanExpression evaluateLike(StringPath path, String value) {
        return value == null ? null : path.likeIgnoreCase("%" + value + "%");
    }

    private BooleanExpression ltFieldId(Long fieldId) {
        return fieldId == null ? null : field.id.lt(fieldId);
    }

}
