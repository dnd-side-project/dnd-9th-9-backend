package com.dnd.Exercise.domain.bookmark.service;

import com.dnd.Exercise.domain.bookmark.dto.BookmarkMapper;
import com.dnd.Exercise.domain.bookmark.dto.request.DeleteBookmarksReq;
import com.dnd.Exercise.domain.bookmark.dto.request.SaveBookmarksReq;
import com.dnd.Exercise.domain.bookmark.dto.response.BookmarkDto;
import com.dnd.Exercise.domain.bookmark.dto.response.FindAllBookmarksRes;
import com.dnd.Exercise.domain.bookmark.entity.Bookmark;
import com.dnd.Exercise.domain.bookmark.repository.BookmarkRepository;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper bookmarkMapper;

    @Override
    public FindAllBookmarksRes findAllBookmarks(User user) {
        List<BookmarkDto> bookmarks = bookmarkRepository.findAllByUserId(user.getId()).stream()
                .map(bookmarkMapper::from)
                .collect(Collectors.toList());

        return FindAllBookmarksRes.builder()
                .bookmarks(bookmarks)
                .totalCount(bookmarks.size())
                .build();
    }

    @Override
    @Transactional
    public void saveBookmarks(SaveBookmarksReq saveBookmarksReq, User user) {
        List<Bookmark> bookmarks = saveBookmarksReq.toEntitiesWithUser(user);
        bookmarkRepository.saveAll(bookmarks);
    }

    @Override
    @Transactional
    public void deleteBookmarks(DeleteBookmarksReq deleteBookmarksReq) {
        bookmarkRepository.deleteAllById(deleteBookmarksReq.getIds());
    }
}
