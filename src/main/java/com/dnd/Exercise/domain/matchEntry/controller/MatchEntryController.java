package com.dnd.Exercise.domain.matchEntry.controller;

import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.matchEntry.dto.request.MatchEntryReq;
import com.dnd.Exercise.domain.matchEntry.dto.request.EntryDirection;
import com.dnd.Exercise.domain.matchEntry.dto.response.FindAllMatchEntryRes;
import com.dnd.Exercise.domain.matchEntry.dto.response.FindAllTeamEntryRes;
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

    @ApiOperation(value = "[팀 - 팀원] 페이지 - 팀 신청받은 내역 조회 📬", notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping("/team/{id}")
    public ResponseEntity<FindAllTeamEntryRes> findAllTeamEntries(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size){
        FindAllTeamEntryRes findAllTeamEntryRes = new FindAllTeamEntryRes();
        return ResponseDto.ok(findAllTeamEntryRes);
    }

    /**
     * AuthenticationPrinciple을 통한 본인이 참여하고 있는 MATCH인지 확인하는 로직 추가 고려
     */
    @ApiOperation(value = "[매치 - 매칭] 페이지 - 배틀 신청 내역 조회 📬",
            notes = "EntryDirection을 통해 요청 받은 내역과 요청한 내역 구분 <br> 페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping("/battle/{id}")
    public ResponseEntity<FindAllMatchEntryRes> findAllBattleEntriesByDirection(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @RequestParam(value = "direction") EntryDirection entryDirection,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size){
        FindAllMatchEntryRes findAllMatchEntryRes = new FindAllMatchEntryRes();
        return ResponseDto.ok(findAllMatchEntryRes);
    }


    @ApiOperation(value = "[My 매칭 - 신청] 페이지 - 매치 신청한 내역 조회 📬", notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping
    public ResponseEntity<FindAllMatchEntryRes> findAllBattleEntriesByType(
            // AuthenticationPrinciple 추가
            @RequestParam(value = "matchType") MatchType matchType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size){
        FindAllMatchEntryRes findAllMatchEntryRes = new FindAllMatchEntryRes();
        return ResponseDto.ok(findAllMatchEntryRes);
    }

    // AuthenticationPrinciple 을 통해 userId를 가져와서 Match 유무를 조회한다.
    @ApiOperation(value = "매치 신청 📬", notes = "팀 신청시 myMatchId: null")
    @PostMapping
    public ResponseEntity<String> createMatchEntry(
            // AuthenticationPrinciple 추가
            @RequestBody @Valid MatchEntryReq matchEntryReq){
        return ResponseDto.ok("매치 신청 완료");
    }

    @ApiOperation(value = "매치 신청 취소 📬",
            notes = "EntryId를 입력받아 본인이 신청했던 매치 신청 내역을 삭제한다")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatchEntry(
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        return ResponseDto.ok("매치 신청 취소 완료");
    }

    @ApiOperation(value = "매치 수락 📬")
    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptMatchEntry(
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        return ResponseDto.ok("매치 수락 완료");
    }
}
