package com.dnd.Exercise.domain.matchEntry.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindAllMatchEntryRes {

    private List<FindAllMatchEntryDto> matchEntriesInfos;
    private Long totalCount;
}
