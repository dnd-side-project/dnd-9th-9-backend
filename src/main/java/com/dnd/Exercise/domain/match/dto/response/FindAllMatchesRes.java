package com.dnd.Exercise.domain.match.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindAllMatchesRes {
    private List<FindAllMatchesDto> matchesInfos;
    private Long totalCount;
}
