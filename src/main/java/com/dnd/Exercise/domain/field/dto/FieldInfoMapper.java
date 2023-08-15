package com.dnd.Exercise.domain.field.dto;

import static org.mapstruct.NullValuePropertyMappingStrategy.*;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

public interface FieldInfoMapper<D, E> {

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE,
            unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateFromInfoDto(D dto, @MappingTarget E entity);
}
