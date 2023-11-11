package com.dnd.Exercise.domain.teamworkRate.dto;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import com.dnd.Exercise.domain.teamworkRate.dto.response.TeamworkRateHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface TeamworkRateMapper {
    @Mapping(source = "field.fieldType", target = "fieldType")
    @Mapping(source = "field.name", target = "myFieldName")
    @Mapping(source = "field.opponent.name", target = "opponentName")
    @Mapping(source = "field.period", target = "period")
    @Mapping(source = "field.endDate", target = "endDate")
    @Mapping(source = "teamworkRate", target = "teamworkRate")
    @Mapping(source = "winStatus", target = "winStatus")
    TeamworkRateHistoryDto from(Field field, Integer teamworkRate, WinStatus winStatus);
}
