package com.dnd.Exercise.domain.exercise.dto.request;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.entity.RecordProvider;
import com.dnd.Exercise.domain.sports.entity.Sports;
import com.dnd.Exercise.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
public class AppleWorkoutDto {
    @NotBlank
    private String appleUid;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;

    @NotNull
    private Sports sports;

    @NotNull
    private int burnedCalorie;

    public Exercise toEntityWithUser(User user) {
        return Exercise.builder()
                .user(user)

                .sports(sports)
                .exerciseDate(endDateTime.toLocalDate())
                .recordingDateTime(endDateTime)
                .durationMinute(Long.valueOf(ChronoUnit.MINUTES.between(startDateTime,endDateTime)).intValue())
                .burnedCalorie(burnedCalorie)

                .recordProvider(RecordProvider.APPLE)
                .appleUid(appleUid)
                .build();
    }
}
