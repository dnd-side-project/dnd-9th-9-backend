package com.dnd.Exercise.domain.BattleEntry.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@Builder
public class FindBattleEntriesRes {
    private List<FindBattleEntriesDto> battleEntries;
    private Long totalCount;
    private int currentPageSize;
    private int currentPageNumber;

    public FindBattleEntriesRes(Pageable pageable){
        this.battleEntries = new ArrayList<>();
        this.totalCount = 0L;
        this.currentPageNumber = pageable.getPageNumber();
        this.currentPageSize = pageable.getPageSize();
    }

    public static FindBattleEntriesRes from(
            Page<FindBattleEntriesDto> battleEntriesDtoPage,
            Pageable pageable){
        List<FindBattleEntriesDto> battleEntries = battleEntriesDtoPage.getContent();
        Long totalCount = battleEntriesDtoPage.getTotalElements();

        return FindBattleEntriesRes.builder()
                .battleEntries(battleEntries)
                .totalCount(totalCount)
                .currentPageSize(pageable.getPageSize())
                .currentPageNumber(pageable.getPageNumber())
                .build();
    }
}
