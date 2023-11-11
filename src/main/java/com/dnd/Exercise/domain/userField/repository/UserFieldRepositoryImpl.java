package com.dnd.Exercise.domain.userField.repository;

import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.COMPLETED;
import static com.dnd.Exercise.domain.field.entity.QField.field;
import static com.dnd.Exercise.domain.field.repository.FieldRepositoryImpl.evaluateEq;
import static com.dnd.Exercise.domain.userField.entity.QUserField.userField;

import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserFieldRepositoryImpl implements UserFieldRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserField> findCompletedFieldByUserAndType(User user, FieldType fieldType,
            Pageable pageable) {
        JPAQuery<Long> countQuery = queryFactory
                .select(userField.count())
                .from(userField)
                .join(userField.field, field)
                .where(userField.user.id.eq(user.getId()))
                .where(userField.field.fieldStatus.eq(COMPLETED),
                        evaluateEq(userField.field.fieldType, fieldType));

        List<UserField> results = queryFactory
                .selectFrom(userField)
                .join(userField.field, field).fetchJoin()
                .where(userField.user.id.eq(user.getId()))
                .where(userField.field.fieldStatus.eq(COMPLETED),
                        evaluateEq(userField.field.fieldType, fieldType))
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        sort.forEach(orderCond -> {
            Order direction = orderCond.isAscending() ? Order.ASC : Order.DESC;
            String property = orderCond.getProperty();
            PathBuilder orderByExpression = new PathBuilder(UserField.class, "userField");
            orderSpecifiers.add(new OrderSpecifier(direction, orderByExpression.get(property)));
        });
        return orderSpecifiers;
    }
}
