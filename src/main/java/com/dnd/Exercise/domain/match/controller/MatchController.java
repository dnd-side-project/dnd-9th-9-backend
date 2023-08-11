package com.dnd.Exercise.domain.match.controller;

import com.dnd.Exercise.domain.match.dto.request.CreateMatchReq;
import com.dnd.Exercise.domain.match.dto.request.FindAllMatchesCond;
import com.dnd.Exercise.domain.match.dto.request.UpdateMatchInfoReq;
import com.dnd.Exercise.domain.match.dto.request.UpdateMatchProfileReq;
import com.dnd.Exercise.domain.match.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.match.dto.response.FindAllMatchesRes;
import com.dnd.Exercise.domain.match.dto.response.FindMatchRes;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "매치 (팀, 팀배틀, 1:1 배틀) 🔥")
@Slf4j
@RestController
@RequestMapping("/match")
public class MatchController {

    @ApiOperation(value = "매치 생성 🔥", notes = "프로필 사진 업로드가 먼저 진행되어야 합니다")
    @PostMapping
    public ResponseEntity<String> createMatch(@RequestBody @Valid CreateMatchReq createMatchReq){
        return ResponseDto.ok("매치 생성 완료");
    }


    @ApiOperation(value = "조건에 따른 모든 매치 조회 🔥", notes = "페이지 기본값: 0, 사이즈 기본값: 10")
    @GetMapping
    public ResponseEntity<FindAllMatchesRes> findAllMatches(
            @ModelAttribute("findAllMatchesCond") FindAllMatchesCond findAllMatchesCond,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        FindAllMatchesRes findAllMatchesRes = new FindAllMatchesRes();
        return ResponseDto.ok(findAllMatchesRes);
    }


    @ApiOperation(value = "단일 매치 조회 🔥", notes = "팀원 제외, 해당 매치에 관한 정보와 매칭된 매치일 경우 상대 팀 정보를 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<FindMatchRes> findMatch(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId){
        FindMatchRes findMatchRes = new FindMatchRes();
        return ResponseDto.ok(findMatchRes);
    }

    @ApiOperation(value = "매치 프로필 수정 🔥")
    @PatchMapping("/{id}/profile")
    public ResponseEntity<String> updateMatchProfile(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @RequestBody @Valid UpdateMatchProfileReq updateMatchProfileReq){
        return ResponseDto.ok("매치 프로필 수정 완료");
    }


    @ApiOperation(value = "매치 정보 수정 🔥")
    @PatchMapping("/{id}/info")
    public ResponseEntity<String> updateMatchInfo(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @RequestBody @Valid UpdateMatchInfoReq updateMatchInfoReq){
        return ResponseDto.ok("매치 정보 수정 완료");
    }


    @ApiOperation(value = "매치 삭제 🔥")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMatch(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId){
        return ResponseDto.ok("매치 삭제 완료");
    }

    @ApiOperation(value = "자동 매칭 🔥")
    @GetMapping("/auto")
    public ResponseEntity<AutoMatchingRes> autoMatching(
            @Parameter(description = "1대1 배틀일 경우 DUEL, 팀 배틀일 경우 TEAM_BATTLE")
            @RequestParam("matchType") MatchType matchType){
        AutoMatchingRes autoMatchingRes = new AutoMatchingRes();
        return ResponseDto.ok(autoMatchingRes);
    }

    @ApiOperation(value = "방장 넘기기 🔥")
    @PatchMapping("/{id}/change-leader")
    public ResponseEntity<String> changeLeader(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @Parameter(description = "새로운 리더 Id값") @RequestParam("id") Long id){
        return ResponseDto.ok("팀장 변경 완료");
    }
}
