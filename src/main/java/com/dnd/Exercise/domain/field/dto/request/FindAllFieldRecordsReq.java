package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.FieldType;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class FindAllFieldRecordsReq {
    private int page;

    private int size;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private FieldType fieldType;
}
