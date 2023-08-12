package com.dnd.Exercise.domain.match.controller;

import com.dnd.Exercise.domain.match.dto.request.CreateMatchReq;
import com.dnd.Exercise.domain.match.dto.request.FindAllMatchesCond;
import com.dnd.Exercise.domain.match.dto.request.UpdateMatchInfoReq;
import com.dnd.Exercise.domain.match.dto.request.UpdateMatchProfileReq;
import com.dnd.Exercise.domain.match.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.match.dto.response.FindAllMatchesRes;
import com.dnd.Exercise.domain.match.dto.response.FindMatchRes;
import com.dnd.Exercise.domain.match.dto.response.GetMatchExerciseSummaryRes;
import com.dnd.Exercise.domain.match.dto.response.GetRankingRes;
import com.dnd.Exercise.domain.match.entity.MatchSide;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
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
            @Parameter(description = "매치 Id값") @PathVariable("id") Long id){
        FindMatchRes findMatchRes = new FindMatchRes();
        return ResponseDto.ok(findMatchRes);
    }

    @ApiOperation(value = "매치 프로필 수정 🔥")
    @PatchMapping("/{id}/profile")
    public ResponseEntity<String> updateMatchProfile(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMatchProfileReq updateMatchProfileReq){
        return ResponseDto.ok("매치 프로필 수정 완료");
    }


    @ApiOperation(value = "매치 정보 수정 🔥")
    @PatchMapping("/{id}/info")
    public ResponseEntity<String> updateMatchInfo(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMatchInfoReq updateMatchInfoReq){
        return ResponseDto.ok("매치 정보 수정 완료");
    }


    @ApiOperation(value = "매치 삭제 🔥")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMatch(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long id){
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

    @ApiOperation(value = "배틀 중단하기 🔥",
            notes = "배틀 중단 시 정책 결정(매치 삭제 혹은 매치 간의 연결만 끊기) <br> **Delete or Patch**")
    @DeleteMapping("/{id}/terminate")
    public ResponseEntity<String> terminateBattle(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId){
        return ResponseDto.ok("배틀 중단 완료");
    }

    // DB에서 SUM 연산해서 가져오기, 양방향 매핑 고려
    @ApiOperation(value = " (대결 지표로 사용되는) 나의 매치 or 상대편 매치 하루 요약 조회 🔥",
            notes = "특정 하루에 대한 [기록횟수, 오늘까지의 활동링 달성 횟수, 운동시간, 소모 칼로리] 정보 조회 <br>"
                    + "우리팀 요약: HOME, 상대팀 요약: AWAY <br>'홈화면', '하루 요약'에서 사용")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/rating-summary")
    public ResponseEntity<GetMatchExerciseSummaryRes> getMatchExerciseSummary (
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date,
            @RequestParam MatchSide matchSide) {
        GetMatchExerciseSummaryRes getMatchExerciseSummaryRes = new GetMatchExerciseSummaryRes();
        return ResponseDto.ok(getMatchExerciseSummaryRes);
    }

    // DB에서 RANK 사용해서 상위 3개만 추출
    @ApiOperation(value = "나의 매치 or 상대편 매치 팀원별 랭킹 조회 🔥", notes = "팀과 팀배틀에서만 사용")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/team/ranking")
    public ResponseEntity<GetRankingRes> getTeamRanking(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date,
            @RequestParam MatchSide matchSide){
        GetRankingRes getTeamRankingRes = new GetRankingRes();
        return ResponseDto.ok(getTeamRankingRes);
    }

    @ApiOperation(value = "1:1 배틀 랭킹 조회 🔥", notes = "1:1 배틀에서만 사용")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/duel/ranking")
    public ResponseEntity<GetRankingRes> getDuelRanking(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date){
        GetRankingRes getDuelRankingRes = new GetRankingRes();
        return ResponseDto.ok(getDuelRankingRes);
    }

}
