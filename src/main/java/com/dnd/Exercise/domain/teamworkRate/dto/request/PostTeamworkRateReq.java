package com.dnd.Exercise.domain.teamworkRate.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostTeamworkRateReq {
    @ApiModelProperty(value = "필드 id", required = true)
    @NotNull
    private long fieldId;

    @ApiModelProperty(value = "불꽃 평가 점수 (1~5점)", required = true)
    @NotNull
    @Min(1) @Max(5)
    private int teamworkRate;
}
