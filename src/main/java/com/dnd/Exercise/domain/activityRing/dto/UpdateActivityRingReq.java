package com.dnd.Exercise.domain.activityRing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateActivityRingReq {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private int burnedCalorie;
}
