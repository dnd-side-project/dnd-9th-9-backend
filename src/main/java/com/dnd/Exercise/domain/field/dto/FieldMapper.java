package com.dnd.Exercise.domain.field.dto;

import static org.mapstruct.NullValuePropertyMappingStrategy.SET_TO_NULL;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.FieldDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.entity.Field;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface FieldMapper{

    FindAllFieldsDto toFindAllFieldsDto(Field field);

    @BeanMapping(nullValuePropertyMappingStrategy = SET_TO_NULL,
            unmappedTargetPolicy = ReportingPolicy.IGNORE)
    FieldDto toFieldDto(Field field);

    AutoMatchingRes toAutoMatchingRes(Field field);

    @Mapping(source = "exercise.recordingDateTime", target = "exerciseDateTime")
    FindFieldRecordDto toFindFieldRecordDto(Exercise exercise);

    @BeanMapping(nullValuePropertyMappingStrategy = SET_TO_NULL,
            unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "profileImg", target = "field.profileImg", ignore = true)
    void updateFromProfileDto(UpdateFieldProfileReq dto, @MappingTarget Field field);

    @BeanMapping(nullValuePropertyMappingStrategy = SET_TO_NULL,
            unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateFromInfoDto(UpdateFieldInfoReq dto, @MappingTarget Field field);
}
