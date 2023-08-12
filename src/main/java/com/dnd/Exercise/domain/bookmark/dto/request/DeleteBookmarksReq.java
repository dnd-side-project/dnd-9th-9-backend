package com.dnd.Exercise.domain.bookmark.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DeleteBookmarksReq {
    List<Long> ids;
}
