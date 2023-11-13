package com.dnd.Exercise.domain.user.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetFinalSummaryRes {
    private int activeDays;

    private int recordCounts;

    private int matchCounts;

    private int teamworkRate;
}
