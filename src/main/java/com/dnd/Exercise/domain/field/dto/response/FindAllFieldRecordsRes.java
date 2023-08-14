package com.dnd.Exercise.domain.field.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class FindAllFieldRecordsRes {
    private List<FindFieldRecordDto> recordList;

    private int totalCount;
}
