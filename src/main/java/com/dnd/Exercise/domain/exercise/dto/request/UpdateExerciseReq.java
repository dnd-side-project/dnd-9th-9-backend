package com.dnd.Exercise.domain.exercise.dto.request;

import com.dnd.Exercise.domain.sports.entity.Sports;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateExerciseReq {
    @NotNull(message = "운동 종목을 입력해주세요.")
    private Sports sports;
    @NotNull(message = "운동 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate exerciseDate;
    @NotNull(message = "운동 시간을 입력해주세요.")
    private int durationMinute;
    @NotNull(message = "소모 칼로리를 입력해주세요.")
    private int burnedCalorie;

    private String memoImg;
    private String memoContent;
    private Boolean isMemoPublic;
}
