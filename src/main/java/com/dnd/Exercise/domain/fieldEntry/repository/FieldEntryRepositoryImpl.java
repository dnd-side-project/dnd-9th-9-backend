package com.dnd.Exercise.domain.fieldEntry.repository;


import static com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection.SENT;
import static com.dnd.Exercise.domain.fieldEntry.entity.QFieldEntry.fieldEntry;
import static com.dnd.Exercise.domain.user.entity.QUser.user;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.QField;
import com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryDto;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryDto;
import com.querydsl.core.types.Projections;
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
public class FieldEntryRepositoryImpl implements FieldEntryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FindAllTeamEntryDto> findAllTeamEntryByHostField(Field field, Pageable pageable) {

        JPAQuery<Long> countQuery = queryFactory
                .select(fieldEntry.count())
                .from(fieldEntry)
                .join(fieldEntry.entrantUser, user)
                .where(fieldEntry.hostField.id.eq(field.getId()), fieldEntry.entrantField.isNull());

        List<FindAllTeamEntryDto> results = queryFactory
                .select(Projections.constructor(FindAllTeamEntryDto.class,
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

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FindAllBattleEntryDto> findAllBattleByField(Field field,
            FieldDirection fieldDirection, Pageable pageable) {

        JPAQuery<Long> countQuery = queryFactory
                .select(fieldEntry.count())
                .from(fieldEntry);

        JPAQuery<FindAllBattleEntryDto> query = queryFactory
                .select(Projections.constructor(FindAllBattleEntryDto.class,
                        fieldEntry.id,
                        fieldDirection == SENT ? fieldEntry.hostField.id : fieldEntry.entrantField.id,
                        fieldDirection == SENT ? fieldEntry.hostField.name : fieldEntry.entrantField.name,
                        fieldDirection == SENT ? fieldEntry.hostField.fieldType : fieldEntry.entrantField.fieldType,
                        fieldDirection == SENT ? fieldEntry.hostField.currentSize : fieldEntry.entrantField.currentSize,
                        fieldDirection == SENT ? fieldEntry.hostField.maxSize : fieldEntry.entrantField.maxSize,
                        fieldDirection == SENT ? fieldEntry.hostField.skillLevel : fieldEntry.entrantField.skillLevel,
                        fieldDirection == SENT ? fieldEntry.hostField.period : fieldEntry.entrantField.period))
                .from(fieldEntry)
                .orderBy(fieldEntry.createdAt.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        if (SENT.equals(fieldDirection)) {
            query.join(fieldEntry.hostField, QField.field);
            query.where(fieldEntry.entrantField.id.eq(field.getId()), fieldEntry.entrantUser.isNull());

            countQuery.join(fieldEntry.hostField, QField.field);
            countQuery.where(fieldEntry.entrantField.id.eq(field.getId()), fieldEntry.entrantUser.isNull());

        } else {
            query.join(fieldEntry.entrantField, QField.field);
            query.where(fieldEntry.hostField.id.eq(field.getId()), fieldEntry.entrantUser.isNull());

            countQuery.join(fieldEntry.entrantField, QField.field);
            countQuery.where(fieldEntry.hostField.id.eq(field.getId()), fieldEntry.entrantUser.isNull());
        }

        List<FindAllBattleEntryDto> results = query.fetch();

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }
}
