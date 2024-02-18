package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public static FindAllFieldRecordsRes from(Page<FindFieldRecordDto> fieldRecordPage, Field field, Pageable pageable){
        List<FindFieldRecordDto> recordList = fieldRecordPage.getContent();
        Long totalCount = fieldRecordPage.getTotalElements();

        Long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), field.getEndDate());

        return FindAllFieldRecordsRes.builder()
                .recordList(recordList)
                .rule(field.getRule())
                .daysLeft(daysLeft)
                .totalCount(totalCount)
                .currentPageNumber(pageable.getPageNumber())
                .currentPageSize(pageable.getPageSize())
                .build();
    }
}
