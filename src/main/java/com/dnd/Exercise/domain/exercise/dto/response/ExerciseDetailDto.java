package com.dnd.Exercise.domain.exercise.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.entity.RecordProvider;
import com.dnd.Exercise.domain.sports.entity.Sports;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDetailDto {

    @ApiModelProperty(value = "운동기록 아이디")
    private Long id;

    private Sports sports;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate exerciseDate;
    @ApiModelProperty(value = "운동 기록 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordingDateTime;
    private int durationMinute;
    private int burnedCalorie;

    private String memoImg;
    private String memoContent;
    private Boolean isMemoPublic;

    private RecordProvider recordProvider;
    @ApiModelProperty(value = "애플 데이터 상 운동기록 고유 ID")
    private String appleUid;

    public ExerciseDetailDto(Exercise entity) {
        this.id = entity.getId();
        this.sports = entity.getSports();
        this.exerciseDate = entity.getExerciseDate();
        this.recordingDateTime = entity.getRecordingDateTime();
        this.durationMinute = entity.getDurationMinute();
        this.burnedCalorie = entity.getBurnedCalorie();
        this.memoImg = entity.getMemoImg();
        this.memoContent = entity.getMemoContent();
        this.isMemoPublic = entity.isMemoPublic();
        this.recordProvider = entity.getRecordProvider();
        this.appleUid = entity.getAppleUid();
    }
}
