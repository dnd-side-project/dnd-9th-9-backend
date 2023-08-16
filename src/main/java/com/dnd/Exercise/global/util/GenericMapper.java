package com.dnd.Exercise.global.util;

import static org.mapstruct.NullValuePropertyMappingStrategy.*;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

public interface GenericMapper<D, E> {

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE,
            unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateFromDto(D dto, @MappingTarget E entity);
}
