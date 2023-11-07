package com.dnd.Exercise.domain.teamworkRate.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamworkRateHistoryDto {

    private FieldType fieldType;

    private String myFieldName;

    private String opponentName;

    private Period period;

    private LocalDate endDate;

    private Integer teamworkRate;

    private WinStatus winStatus;
}
