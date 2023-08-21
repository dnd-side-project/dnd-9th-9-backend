package com.dnd.Exercise.domain.fieldEntry.dto;

import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryRes;
import com.dnd.Exercise.domain.fieldEntry.entity.FieldEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FieldEntryMapper {

    @Mapping(source = "fieldEntry.id", target = "entryId")
    @Mapping(source = "fieldEntry.hostField.id", target = "fieldId")
    @Mapping(source = "fieldEntry.hostField.name", target = "name")
    @Mapping(source = "fieldEntry.hostField.fieldType", target = "fieldType")
    @Mapping(source = "fieldEntry.hostField.currentSize", target = "currentSize")
    @Mapping(source = "fieldEntry.hostField.maxSize", target = "maxSize")
    @Mapping(source = "fieldEntry.hostField.skillLevel", target = "skillLevel")
    @Mapping(source = "fieldEntry.hostField.period", target = "period")
    FindAllBattleEntryRes toFindAllBattleEntryRes(FieldEntry fieldEntry);
}
