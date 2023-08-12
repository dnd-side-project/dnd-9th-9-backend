package com.dnd.Exercise.domain.exercise.dto.request;

import com.dnd.Exercise.domain.exercise.entity.RecordProvider;
import com.dnd.Exercise.domain.sports.entity.Sports;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostExerciseByCommonReq {
    private Sports sports;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate exerciseDate;
    private int durationMinute;
    private int burnedCalorie;

    private String memoImg;
    private String memoContent;
    private Boolean isMemoPublic;
}
