package com.dnd.Exercise.domain.exercise.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.entity.RecordProvider;
import com.dnd.Exercise.domain.sports.entity.Sports;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ExerciseDetailDto {

    private Long id;

    private Sports sports;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate exerciseDate;
    private int durationMinute;
    private int burnedCalorie;

    private String memoImg;
    private String memoContent;
    private Boolean isMemoPublic;

    private RecordProvider recordProvider;
    private String appleUid;

    public ExerciseDetailDto(Exercise entity) {
        this.id = entity.getId();
        this.sports = entity.getSports();
        this.exerciseDate = entity.getExerciseDate();
        this.durationMinute = entity.getDurationMinute();
        this.burnedCalorie = entity.getBurnedCalorie();
        this.memoImg = entity.getMemoImg();
        this.memoContent = entity.getMemoContent();
        this.isMemoPublic = entity.isMemoPublic();
        this.recordProvider = entity.getRecordProvider();
        this.appleUid = entity.getAppleUid();
    }
}
