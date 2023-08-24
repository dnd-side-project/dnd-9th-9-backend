package com.dnd.Exercise.domain.bookmark.dto.request;

import com.dnd.Exercise.domain.bookmark.entity.Bookmark;
import com.dnd.Exercise.domain.sports.entity.Sports;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class SaveBookmarksReq {
    List<Sports> sports;

    public List<Bookmark> toEntitiesWithUser(User user) {
        List<Bookmark> entities = sports.stream()
                .map(s -> Bookmark.builder()
                        .user(user)
                        .sports(s)
                        .build())
                .collect(Collectors.toList());
        return entities;
    }
}
