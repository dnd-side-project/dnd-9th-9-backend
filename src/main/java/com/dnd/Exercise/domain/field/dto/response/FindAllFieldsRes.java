package com.dnd.Exercise.domain.field.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAllFieldsRes {
    private List<FindAllFieldsDto> fieldsInfos;
    private int currentPageSize;

    public static FindAllFieldsRes from(List<FindAllFieldsDto> fieldsInfos, Integer pageSize){
        return FindAllFieldsRes.builder()
                .fieldsInfos(fieldsInfos)
                .currentPageSize(pageSize)
                .build();
    }
}
