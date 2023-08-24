package com.dnd.Exercise.domain.bookmark.service;

import com.dnd.Exercise.domain.bookmark.dto.request.DeleteBookmarksReq;
import com.dnd.Exercise.domain.bookmark.dto.request.SaveBookmarksReq;
import com.dnd.Exercise.domain.bookmark.dto.response.FindAllBookmarksRes;
import com.dnd.Exercise.domain.user.entity.User;

public interface BookmarkService {
    FindAllBookmarksRes findAllBookmarks(User user);
    void saveBookmarks(SaveBookmarksReq saveBookmarksReq, User user);
    void deleteBookmarks(DeleteBookmarksReq deleteBookmarksReq);
}
