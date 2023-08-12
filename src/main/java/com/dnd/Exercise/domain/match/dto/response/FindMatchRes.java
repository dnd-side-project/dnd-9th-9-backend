package com.dnd.Exercise.domain.match.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindMatchRes {

    private MatchDto matchDto;

    private FindAllMatchesDto assignedMatchDto;
}
