package com.dnd.Exercise.domain.field.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingDto {

    private Long userId;

    private String profileImg;

    private Integer value;
}
