package com.dnd.Exercise.domain.user.dto;

import com.dnd.Exercise.domain.user.dto.request.UpdateMyProfileReq;
import com.dnd.Exercise.domain.user.dto.request.UpdateOnboardProfileReq;
import com.dnd.Exercise.domain.user.dto.response.GetProfileDetail;
import com.dnd.Exercise.domain.user.entity.User;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface UserMapper {
    GetProfileDetail from(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE,
            unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateFromDto(UpdateOnboardProfileReq updateOnboardProfileReq, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE,
            unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateFromDto(UpdateMyProfileReq updateMyProfileReq, @MappingTarget User user);
}
