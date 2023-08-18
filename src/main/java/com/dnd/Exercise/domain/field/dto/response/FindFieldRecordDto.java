package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.sports.entity.Sports;
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
    public FindFieldRecordDto(Long id, Long userId, String profileImg, String name,
            Boolean isLeader,
            Sports sports, LocalDateTime exerciseDateTime, int durationMinute, int burnedCalorie,
            String memoImg, String memoContent, Boolean isMemoPublic) {
        this.id = id;
        this.userId = userId;
        this.profileImg = profileImg;
        this.name = name;
        this.isLeader = isLeader;
        this.sports = sports;
        this.exerciseDateTime = exerciseDateTime;
        this.durationMinute = durationMinute;
        this.burnedCalorie = burnedCalorie;
        this.memoImg = memoImg;
        this.memoContent = memoContent;
        this.isMemoPublic = isMemoPublic;
    }
}
