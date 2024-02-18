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

    public static GetRankingRes from(
            List<RankingDto> exerciseTime,
            List<RankingDto> burnedCalorie,
            List<RankingDto> recordCount,
            List<RankingDto> goalAchievedCount){
        return GetRankingRes.builder()
                .recordCountRanking(exerciseTime)
                .exerciseTimeRanking(burnedCalorie)
                .burnedCalorieRanking(recordCount)
                .goalAchievedCountRanking(goalAchievedCount)
                .build();
    }
}
