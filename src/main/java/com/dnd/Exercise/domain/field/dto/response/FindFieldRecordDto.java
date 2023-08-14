package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.sports.entity.Sports;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
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
}
