package com.dnd.Exercise.domain.field.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindAllFieldsRes {
    private List<FindAllFieldsDto> fieldsInfos;
    private Long totalCount;
}
