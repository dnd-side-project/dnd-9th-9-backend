package com.dnd.Exercise.domain.teamworkRate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamworkRateHistoryRes {
    List<TeamworkRateHistoryDto> teamworkRateHistoryDtos;

    private int currentPage;
    private int currentPageSize;

    private Boolean isFirstPage;
    private Boolean isLastPage;
}
