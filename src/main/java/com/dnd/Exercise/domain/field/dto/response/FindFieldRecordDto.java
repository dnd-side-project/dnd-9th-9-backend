package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.sports.entity.Sports;
import com.dnd.Exercise.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindFieldRecordDto {

    private Long id;

    private Long userId;

    private String profileImg;

    private String name;

    private Boolean isLeader;

    private Sports sports;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exerciseDateTime;

    private int durationMinute;

    private int burnedCalorie;

    private String memoImg;

    private String memoContent;

    private Boolean isMemoPublic;

    @QueryProjection
    public FindFieldRecordDto(Exercise exercise, User user, Boolean isLeader) {
        this.id = exercise.getId();
        this.userId = user.getId();
        this.profileImg = user.getProfileImg();
        this.name = user.getName();
        this.isLeader = isLeader;
        this.sports = exercise.getSports();
        this.exerciseDateTime = exercise.getRecordingDateTime();
        this.durationMinute = exercise.getDurationMinute();
        this.burnedCalorie = exercise.getBurnedCalorie();
        this.memoImg = exercise.getMemoImg();
        this.memoContent = exercise.getMemoContent();
        this.isMemoPublic = exercise.isMemoPublic();
    }
}
