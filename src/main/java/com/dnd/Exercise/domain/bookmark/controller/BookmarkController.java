package com.dnd.Exercise.domain.bookmark.controller;

import com.dnd.Exercise.domain.bookmark.dto.request.DeleteBookmarksReq;
import com.dnd.Exercise.domain.bookmark.dto.request.UpdateBookmarksReq;
import com.dnd.Exercise.domain.bookmark.dto.response.FindAllBookmarksRes;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "운동 즐겨찾기 🔍")
@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    @ApiOperation(value = "나의 즐겨찾기 리스트 불러오기 🔍", notes = "")
    @GetMapping("")
    public ResponseEntity<FindAllBookmarksRes> findAllBookmarks() {
        return ResponseDto.ok(new FindAllBookmarksRes());
    }

    @ApiOperation(value = "나의 즐겨찾기 리스트 등록하기 🔍", notes = "등록하고 싶은 운동 종목(enum)들의 리스트를 전송합니다.")
    @PostMapping("")
    public ResponseEntity<String> updateBookmarks(@RequestBody UpdateBookmarksReq updateBookmarksReq) {
        return ResponseDto.ok("등록이 완료되었습니다.");
    }

    @ApiOperation(value = "나의 즐겨찾기 리스트 삭제하기 🔍", notes = "삭제하고 싶은 즐겨찾기 id 리스트를 전송합니다.")
    @DeleteMapping("")
    public ResponseEntity<String> deleteBookmarks(@RequestBody DeleteBookmarksReq deleteBookmarksReq) {
        return ResponseDto.ok("삭제가 완료되었습니다.");
    }
}
