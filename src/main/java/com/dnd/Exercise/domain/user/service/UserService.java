package com.dnd.Exercise.domain.user.service;

import com.dnd.Exercise.domain.user.dto.request.*;
import com.dnd.Exercise.domain.user.dto.response.GetFinalSummaryRes;
import com.dnd.Exercise.domain.user.dto.response.GetMatchSummaryRes;
import com.dnd.Exercise.domain.user.dto.response.GetProfileDetail;

public interface UserService {
    GetProfileDetail getProfileDetail(long userId);
    void updateOnboardProfile(UpdateOnboardProfileReq updateOnboardProfileReq, long userId);
    void postSkillLevel(PostSkillLevelReq postSkillLevelReq, long userId);
    void updateProfile(UpdateMyProfileReq updateMyProfileReq, long userId);
    void updateAppleLinked(UpdateAppleLinkedReq updateAppleLinkedReq, long userId);
    void updateNotificationAgreed(UpdateNotificationAgreementReq updateNotificationAgreementReq, long userId);
    void withdraw(long userId);
    GetFinalSummaryRes getFinalSummary(long userId);
    GetMatchSummaryRes getMatchSummary(long userId);
}
