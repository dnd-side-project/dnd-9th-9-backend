package com.dnd.Exercise.domain.field.repository;

import static com.dnd.Exercise.domain.field.entity.QField.field;

import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.entity.Field;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FieldRepositoryImpl implements FieldRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Field> findAllFieldsWithFilter(FindAllFieldsCond findAllFieldsCond,
            Pageable pageable) {

        BooleanBuilder whereClause = new BooleanBuilder();

        whereClause.and(evaluateEq(field.fieldType, findAllFieldsCond.getFieldType()))
                .and(evaluateEq(field.maxSize, findAllFieldsCond.getMemberCount()))
                .and(evaluateIn(field.skillLevel, findAllFieldsCond.getSkillLevel()))
                .and(evaluateIn(field.strength, findAllFieldsCond.getStrength()))
                .and(evaluateIn(field.period, findAllFieldsCond.getPeriod()))
                .and(evaluateIn(field.goal, findAllFieldsCond.getGoal()));

        JPAQuery<Long> countQuery = queryFactory
                .select(field.count())
                .from(field)
                .where(whereClause);

        List<Field> results = queryFactory
                .select(field)
                .from(field)
                .where(whereClause)
                .orderBy(field.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private <T> BooleanExpression evaluateEq(SimpleExpression<T> path, T value) {
        return value == null ? null : path.eq(value);
    }

    private <T> BooleanExpression evaluateIn(SimpleExpression<T> path, Collection<T> values) {
        return values == null ? null : path.in(values);
    }


}
