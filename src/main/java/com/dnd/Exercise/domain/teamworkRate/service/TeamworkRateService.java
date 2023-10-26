package com.dnd.Exercise.domain.teamworkRate.service;

import com.dnd.Exercise.domain.teamworkRate.dto.request.PostTeamworkRateReq;
import com.dnd.Exercise.domain.user.entity.User;

public interface TeamworkRateService {
    void postTeamworkRate(PostTeamworkRateReq postTeamworkRateReq, User user);
}
