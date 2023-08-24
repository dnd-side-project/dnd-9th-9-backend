package com.dnd.Exercise.domain.activityRing.dto;

import com.dnd.Exercise.domain.activityRing.dto.request.UpdateActivityRingReq;
import com.dnd.Exercise.domain.activityRing.entity.ActivityRing;
import com.dnd.Exercise.global.util.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityRingMapper extends GenericMapper<UpdateActivityRingReq, ActivityRing> {

}
