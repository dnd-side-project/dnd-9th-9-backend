package com.dnd.Exercise.domain.userField.repository;

import static com.dnd.Exercise.domain.field.entity.FieldStatus.COMPLETED;
import static com.dnd.Exercise.domain.field.entity.QField.field;
import static com.dnd.Exercise.domain.field.repository.FieldRepositoryImpl.evaluateEq;
import static com.dnd.Exercise.domain.userField.entity.QUserField.userField;

import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserFieldRepositoryImpl implements UserFieldRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserField> findCompletedFieldByUserAndType(User user, FieldType fieldType,
            Pageable pageable) {
        return queryFactory
                .selectFrom(userField)
                .join(userField.field, field).fetchJoin()
                .where(userField.user.id.eq(user.getId()))
                .where(userField.field.fieldStatus.eq(COMPLETED),
                        evaluateEq(userField.field.fieldType, fieldType))
                .orderBy(field.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
