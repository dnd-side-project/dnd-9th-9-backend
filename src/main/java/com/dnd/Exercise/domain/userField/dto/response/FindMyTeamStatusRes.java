package com.dnd.Exercise.domain.userField.dto.response;

import lombok.Data;

@Data
public class FindMyTeamStatusRes {

    private String teamName;

    private TopPlayerDto burnedCalorie;

    private TopPlayerDto recordCount;

    private TopPlayerDto goalAchievedCount;

    private TopPlayerDto exerciseTimeMinute;
}
