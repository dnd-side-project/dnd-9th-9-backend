package com.dnd.Exercise.domain.MemberEntry.dtos.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@Builder
public class FindMemberEntriesRes {
    private List<FindMemberEntriesDto> memberEntries;
    private Long totalCount;
    private int currentPageSize;
    private int currentPageNumber;

    public static FindMemberEntriesRes from(
            Page<FindMemberEntriesDto> memberEntriesDtoPage,
            Pageable pageable){
        List<FindMemberEntriesDto> memberEntries = memberEntriesDtoPage.getContent();
        Long totalCount = memberEntriesDtoPage.getTotalElements();

        return FindMemberEntriesRes.builder()
                .memberEntries(memberEntries)
                .totalCount(totalCount)
                .currentPageSize(pageable.getPageSize())
                .currentPageNumber(pageable.getPageNumber())
                .build();
    }
}
