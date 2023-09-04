package com.dnd.Exercise.domain.userField.dto.response;

import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FindAllMyCompletedFieldsRes {
    private List<FindAllFieldsDto> completedFields;
    private Long totalCount;
    private int currentPageSize;
    private int currentPageNumber;
}
