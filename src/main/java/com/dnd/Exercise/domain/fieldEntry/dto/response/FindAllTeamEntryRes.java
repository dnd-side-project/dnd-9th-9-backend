package com.dnd.Exercise.domain.fieldEntry.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FindAllTeamEntryRes {
    private List<FindAllTeamEntryDto> teamEntries;
    private Long totalCount;
    private int currentPageSize;
    private int currentPageNumber;
}
