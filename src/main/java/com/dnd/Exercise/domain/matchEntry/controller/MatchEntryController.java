package com.dnd.Exercise.domain.matchEntry.controller;

import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.matchEntry.dto.request.DeleteMatchEntryReq;
import com.dnd.Exercise.domain.matchEntry.dto.request.EntryDirection;
import com.dnd.Exercise.domain.matchEntry.dto.response.FindAllMatchEntryRes;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "매치 신청 내역 📬")
@RestController
@RequestMapping("/match/entry")
public class MatchEntryController {

    /**
     * AuthenticationPrinciple을 통한 본인이 참여하고 있는 MATCH인지 확인하는 로직 추가 고려
     */
    @ApiOperation(value = "[매치 - 매칭] 페이지 - 매치 신청 내역 조회 📬", notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping("/{id}")
    public ResponseEntity<FindAllMatchEntryRes> findAllMatchEntriesByDirection(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @RequestParam(value = "direction") EntryDirection entryDirection,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size){
        FindAllMatchEntryRes findAllMatchEntryRes = new FindAllMatchEntryRes();
        return ResponseDto.ok(findAllMatchEntryRes);
    }


    @ApiOperation(value = "[My 매칭 - 신청] 페이지 - 매치 신청 내역 조회 📬", notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping
    public ResponseEntity<FindAllMatchEntryRes> findAllMatchEntriesByType(
            // AuthenticationPrinciple 추가
            @RequestParam(value = "matchType") MatchType matchType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size){
        FindAllMatchEntryRes findAllMatchEntryRes = new FindAllMatchEntryRes();
        return ResponseDto.ok(findAllMatchEntryRes);
    }

    // AuthenticationPrinciple 을 통해 userId를 가져와서 Match 유무를 조회한다.
    @ApiOperation(value = "매치 신청 📬")
    @PostMapping("/{id}")
    public ResponseEntity<String> createMatchEntry(
            @Parameter(description = "신청할 매치 Id값") @PathVariable("id") Long matchId,
            @RequestParam("matchType") MatchType matchType){
        return ResponseDto.ok("매치 신청 완료");
    }

    @ApiOperation(value = "매치 신청 취소 📬",
            notes = "요청받은 매칭 거절시: RECEIVED <br> 요청한 매칭 취소시: SENT")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatchEntry(
            @Parameter(description = "상대 매치 Id값") @PathVariable("id") Long matchId,
            @RequestBody @Valid DeleteMatchEntryReq deleteMatchEntryReq){
        return ResponseDto.ok("매치 신청 취소 완료");
    }

    @ApiOperation(value = "매치 수락 📬")
    @PostMapping("{id}/select")
    public ResponseEntity<String> acceptMatchEntry(
            @Parameter(description = "상대 매치 Id값") @PathVariable("id") Long matchId,
            @RequestParam("matchType") MatchType matchType){
        return ResponseDto.ok("매치 수락 완료");
    }
}
