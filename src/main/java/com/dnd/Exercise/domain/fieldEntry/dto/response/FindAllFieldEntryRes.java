package com.dnd.Exercise.domain.fieldEntry.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindAllFieldEntryRes {

    private List<FindAllFieldEntryDto> fieldEntriesInfos;
    private Long totalCount;
}
