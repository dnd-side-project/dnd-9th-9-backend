package com.dnd.Exercise.domain.field.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRankingRes {

    private List<RankingDto> exerciseTimeRanking;

    private List<RankingDto> burnedCalorieRanking;

    private List<RankingDto> recordCountRanking;

    private List<RankingDto> goalAchievedCountRanking;
}
