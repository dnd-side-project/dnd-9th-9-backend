package com.dnd.Exercise.domain.fieldEntry.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@Builder
public class FindAllBattleEntryRes {
    private List<FindAllBattleEntryDto> battleEntries;
    private Long totalCount;
    private int currentPageSize;
    private int currentPageNumber;

    public FindAllBattleEntryRes(Pageable pageable){
        this.battleEntries = new ArrayList<>();
        this.totalCount = 0L;
        this.currentPageNumber = pageable.getPageNumber();
        this.currentPageSize = pageable.getPageSize();
    }
}
