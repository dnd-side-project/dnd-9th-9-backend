package com.dnd.Exercise.domain.matchEntry.dto.request;

import com.dnd.Exercise.domain.match.entity.MatchType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchEntryReq {

    private Long myMatchId;

    @NotNull
    private Long opponentMatchId;

    private MatchType matchType;
}
