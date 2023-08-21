package com.dnd.Exercise.domain.exercise.dto.request;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.entity.RecordProvider;
import com.dnd.Exercise.domain.sports.entity.Sports;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostExerciseByCommonReq {
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

    public Exercise toEntityWithUser(User user) {
        return Exercise.builder()
                .user(user)
                .sports(sports)
                .exerciseDate(exerciseDate)
                .recordingDateTime(LocalDateTime.now())
                .durationMinute(durationMinute)
                .burnedCalorie(burnedCalorie)

                .memoImg(memoImg == null ? null : memoImg)
                .memoContent(memoContent == null ? null : memoContent)
                .isMemoPublic(isMemoPublic)

                .recordProvider(RecordProvider.MATCH_UP)
                .build();
    }
}
