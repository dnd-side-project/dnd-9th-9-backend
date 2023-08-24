package com.dnd.Exercise.domain.bookmark.dto;

import com.dnd.Exercise.domain.bookmark.dto.response.BookmarkDto;
import com.dnd.Exercise.domain.bookmark.entity.Bookmark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookmarkMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "sports", target = "sports")
    BookmarkDto from(Bookmark bookmark);
}
