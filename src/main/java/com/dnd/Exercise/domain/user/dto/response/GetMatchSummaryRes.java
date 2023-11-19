package com.dnd.Exercise.domain.user.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetMatchSummaryRes {
    @ApiModelProperty(value = "팀 매칭 참여 횟수")
    private int teamMatchCount;

    @ApiModelProperty(value = "1:1 매칭 참여 횟수")
    private int duelMatchCount;

    @ApiModelProperty(value = "매칭 안하는 팀 참여 횟수")
    private int teamCount;

    @ApiModelProperty(value = "매칭 승률 (소수점 첫째 자리에서 반올림)")
    private int winningRate;
}
