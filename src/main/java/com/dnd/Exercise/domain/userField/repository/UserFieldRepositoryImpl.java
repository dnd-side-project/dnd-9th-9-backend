package com.dnd.Exercise.domain.userField.repository;

import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.COMPLETED;
import static com.dnd.Exercise.domain.field.entity.QField.field;
import static com.dnd.Exercise.domain.field.repository.FieldRepositoryImpl.evaluateEq;
import static com.dnd.Exercise.domain.userField.entity.QUserField.userField;

import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserFieldRepositoryImpl implements UserFieldRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserField> findCompletedFieldByUserAndType(User user, FieldType fieldType, Pageable pageable) {
        BooleanBuilder whereClause = filterBuilder(user, fieldType);
        JPAQuery<Long> countQuery = queryFactory
                .select(userField.count())
                .from(userField)
                .join(userField.field, field)
                .where(whereClause);

        List<UserField> results = queryFactory
                .selectFrom(userField)
                .join(userField.field, field).fetchJoin()
                .where(whereClause)
                .orderBy(userField.field.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder filterBuilder(User user, FieldType fieldType){
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(userField.user.id.eq(user.getId()))
                .and(userField.field.fieldStatus.eq(COMPLETED))
                .and(evaluateEq(userField.field.fieldType, fieldType));

        return whereClause;
    }
}
