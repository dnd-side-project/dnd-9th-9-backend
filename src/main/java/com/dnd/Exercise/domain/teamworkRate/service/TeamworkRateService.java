package com.dnd.Exercise.domain.teamworkRate.service;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.teamworkRate.dto.request.PostTeamworkRateReq;
import com.dnd.Exercise.domain.teamworkRate.dto.response.GetTeamworkRateHistoryRes;
import com.dnd.Exercise.domain.user.entity.User;

public interface TeamworkRateService {
    void postTeamworkRate(PostTeamworkRateReq postTeamworkRateReq, User user);
    int getTeamworkRateOfField(Field field);
    GetTeamworkRateHistoryRes getTeamworkRateHistory(FieldType fieldType, Integer page, Integer size, User user);
}
