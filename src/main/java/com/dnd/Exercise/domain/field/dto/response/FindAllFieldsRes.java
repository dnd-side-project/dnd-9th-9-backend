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
}
