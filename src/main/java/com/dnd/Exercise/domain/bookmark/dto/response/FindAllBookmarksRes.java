package com.dnd.Exercise.domain.bookmark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllBookmarksRes {
    private List<BookmarkDto> bookmarks;

    private int totalCount;
}
