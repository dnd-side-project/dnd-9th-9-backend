package com.dnd.Exercise.domain.bookmark.dto.request;

import com.dnd.Exercise.domain.sports.entity.Sports;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateBookmarksReq {
    List<Sports> sports;
}
