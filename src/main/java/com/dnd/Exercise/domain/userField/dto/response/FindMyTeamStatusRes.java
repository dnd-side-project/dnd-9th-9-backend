package com.dnd.Exercise.domain.userField.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindMyTeamStatusRes {

    private String teamName;

    private Long fieldId;

    private Long daysLeft;

    private TopPlayerDto burnedCalorie;

    private TopPlayerDto recordCount;

    private TopPlayerDto goalAchievedCount;

    private TopPlayerDto exerciseTimeMinute;
}
