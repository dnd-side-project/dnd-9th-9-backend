package com.dnd.Exercise.domain.fieldEntry.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class FindAllTeamEntryRes {

    private List<FindAllTeamEntryDto> teamEntriesInfos;
    private Long totalCount;
}
