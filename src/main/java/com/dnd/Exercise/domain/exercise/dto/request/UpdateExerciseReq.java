package com.dnd.Exercise.domain.exercise.dto.request;

import com.dnd.Exercise.domain.sports.entity.Sports;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateExerciseReq {
    private long id;
    private Sports sports;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate exerciseDate;
    private int durationMinute;
    private int burnedCalorie;

    private String memoImg;
    private String memoContent;
    private Boolean isMemoPublic;
}
