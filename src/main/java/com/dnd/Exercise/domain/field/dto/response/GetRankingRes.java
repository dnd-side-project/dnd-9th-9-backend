package com.dnd.Exercise.domain.field.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class GetRankingRes {

    private List<RankingDto> exerciseTimeRanking;

    private List<RankingDto> burnedCalorieRanking;

    private List<RankingDto> recordCountRanking;

    private List<RankingDto> goalAchievedCountRanking;
}
