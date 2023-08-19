package com.dnd.Exercise.domain.field.dto;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.FieldDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.util.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FieldMapper extends GenericMapper<UpdateFieldProfileReq, Field>,
        FieldInfoMapper<UpdateFieldInfoReq, Field> {

    FindAllFieldsDto toFindAllFieldsDto(Field field);

    FieldDto toFieldDto(Field field);

    AutoMatchingRes toAutoMatchingRes(Field field);

    @Mapping(source = "exercise.recordingDateTime", target = "exerciseDateTime")
    FindFieldRecordDto toFindFieldRecordDto(Exercise exercise);
}
