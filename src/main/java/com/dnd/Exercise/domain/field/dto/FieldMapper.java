package com.dnd.Exercise.domain.field.dto;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.response.FieldDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.entity.Field;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FieldMapper {

    FindAllFieldsDto toFindAllFieldsDto(Field field);

    FieldDto toFieldDto(Field field);
}
