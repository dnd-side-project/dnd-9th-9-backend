package com.dnd.Exercise.domain.fieldEntry.repository;


import static com.dnd.Exercise.domain.fieldEntry.entity.QFieldEntry.fieldEntry;
import static com.dnd.Exercise.domain.user.entity.QUser.user;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryRes;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FieldEntryRepositoryImpl implements FieldEntryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FindAllTeamEntryRes> findAllTeamEntryByHostField(Field field, Pageable pageable) {
        return queryFactory
                .select(Projections.constructor(FindAllTeamEntryRes.class,
                        fieldEntry.id,
                        fieldEntry.entrantUser.id,
                        fieldEntry.entrantUser.name,
                        fieldEntry.entrantUser.profileImg,
                        fieldEntry.entrantUser.skillLevel))
                .from(fieldEntry)
                .join(fieldEntry.entrantUser, user)
                .where(fieldEntry.hostField.id.eq(field.getId()), fieldEntry.entrantField.isNull())
                .orderBy(fieldEntry.createdAt.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }
}
