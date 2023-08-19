package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.FieldType;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class FindAllFieldRecordsReq {

    private int page = 0;

    private int size = 3;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    private FieldType fieldType;
}
