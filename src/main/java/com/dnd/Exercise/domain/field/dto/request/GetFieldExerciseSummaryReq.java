package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.FieldSide;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class GetFieldExerciseSummaryReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private FieldSide fieldSide;
}
