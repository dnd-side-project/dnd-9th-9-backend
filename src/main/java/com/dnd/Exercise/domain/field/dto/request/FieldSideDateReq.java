package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.FieldSide;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class FieldSideDateReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(notes = "선택날짜", required = true)
    private LocalDate date;
    @ApiModelProperty(required = true, example = "HOME | AWAY")
    private FieldSide fieldSide;
}
