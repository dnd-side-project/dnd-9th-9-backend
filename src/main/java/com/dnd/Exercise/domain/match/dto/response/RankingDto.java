package com.dnd.Exercise.domain.match.dto.response;

import lombok.Data;

@Data
public class RankingDto {

    private Long userId;

    private String profileImg;

    private int value;
}
