package com.dnd.Exercise.domain.userMatch.controller;

import com.dnd.Exercise.domain.match.dto.response.FindAllMatchesDto;
import com.dnd.Exercise.domain.match.dto.response.FindAllMatchesRes;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.userMatch.dto.response.FindAllMembersRes;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "팀원 관련 정보, 매치 이력 관리 📜")
@RestController
@RequestMapping("/user-match")
public class UserMatchController {

    @ApiOperation(value = "팀원 조회 📜")
    @GetMapping("/{id}")
    public ResponseEntity<List<FindAllMembersRes>> findAllMembers(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId){
        List<FindAllMembersRes> findAllMembersRes = new ArrayList<FindAllMembersRes>();
        return ResponseDto.ok(findAllMembersRes);
    }

    @ApiOperation(value = "진행 중인 나의 매치 조회 📜", notes = "진행완료를 제외한 속해있는 모든 매치 조회")
    @GetMapping("/progress")
    public ResponseEntity<List<FindAllMatchesDto>> findAllMyInProgressMatches(){
        List<FindAllMatchesDto> findAllMyInProgressMatchesRes = new ArrayList<FindAllMatchesDto>();
        return ResponseDto.ok(findAllMyInProgressMatchesRes);
    }

    @ApiOperation(value = "종료된 나의 매치 조회 📜", notes = "페이지 기본값: 0, 사이즈 기본값: 5")
    @GetMapping("/completed")
    public ResponseEntity<FindAllMatchesRes> findAllMyCompletedMatches(
            @RequestParam(value = "matchType") MatchType matchType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size){
        FindAllMatchesRes findAllMyCompletedMatchesRes = new FindAllMatchesRes();
        return ResponseDto.ok(findAllMyCompletedMatchesRes);
    }

    @ApiOperation(value = "팀원 내보내기 📜")
    @DeleteMapping("/{id}/eject")
    public ResponseEntity<String> ejectMember(
            @Parameter(description = "매치 Id값") @PathVariable("id") Long matchId,
            @Parameter(description = "내보낼 팀원 ID 리스트") @RequestParam("ids") List<Long> ids){
        return ResponseDto.ok("팀원 내보내기 완료");
    }

    // @ApiOperation(value = "팀원 날짜 별 기록 조회")
    // @GetMapping("{id}/record")
}
