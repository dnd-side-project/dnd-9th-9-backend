package com.dnd.Exercise.domain.userField.dto;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMyFieldsDto;
import com.dnd.Exercise.domain.userField.entity.UserField;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserFieldMapper {

    @Mapping(source = "user.id", target = "id")
    FindAllMembersRes toFindAllMembersRes(UserField userField, User user);

    FindAllMyFieldsDto toFindAllMyFieldsDto(Field field);
}
