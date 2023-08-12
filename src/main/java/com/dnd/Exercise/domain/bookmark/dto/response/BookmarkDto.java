package com.dnd.Exercise.domain.bookmark.dto.response;

import com.dnd.Exercise.domain.sports.entity.Sports;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkDto {
    private long id;

    private Sports sports;
}
