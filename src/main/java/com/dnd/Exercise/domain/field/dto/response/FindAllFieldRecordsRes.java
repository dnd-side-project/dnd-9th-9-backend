package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindAllFieldRecordsRes {
    private List<FindFieldRecordDto> recordList;

    private WinStatus winStatus;

    private Long daysLeft;

    private String rule;

    private Long totalCount;

    private int currentPageSize;

    private int currentPageNumber;
}
