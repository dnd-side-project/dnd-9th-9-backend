package com.dnd.Exercise.domain.userField.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@Builder
public class FindAllMyCompletedFieldsRes {
    private List<FindAllMyFieldsDto> completedFields;
    private Long totalCount;
    private int currentPageSize;
    private int currentPageNumber;

    public static FindAllMyCompletedFieldsRes from(
            Page<FindAllMyFieldsDto> findAllMyFieldsDtoPage, Pageable pageable){
        List<FindAllMyFieldsDto> completedFields = findAllMyFieldsDtoPage.getContent();
        Long totalCount = findAllMyFieldsDtoPage.getTotalElements();

        return FindAllMyCompletedFieldsRes.builder()
                .completedFields(completedFields)
                .totalCount(totalCount)
                .currentPageSize(pageable.getPageSize())
                .currentPageNumber(pageable.getPageNumber())
                .build();
    }
}
