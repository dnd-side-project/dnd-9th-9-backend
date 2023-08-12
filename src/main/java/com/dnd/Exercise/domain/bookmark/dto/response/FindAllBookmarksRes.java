package com.dnd.Exercise.domain.bookmark.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindAllBookmarksRes {
    private List<BookmarkDto> bookmarks;

    private int totalCount;
}
