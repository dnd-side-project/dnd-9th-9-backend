package com.dnd.Exercise.domain.teamworkRate.controller;

import com.dnd.Exercise.domain.teamworkRate.dto.request.PostTeamworkRateReq;
import com.dnd.Exercise.domain.teamworkRate.service.TeamworkRateService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "불꽃 평가 💯")
@RestController
@RequiredArgsConstructor
@RequestMapping("/teamwork-rate")
public class TeamworkRateController {

    private final TeamworkRateService teamworkRateService;

    @ApiOperation(value = "매칭 종료 시, 불꽃평가 등록 💯", notes = "1~5 점 사이로 불꽃 평가를 등록합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "불꽃평가 등록 완료"),
            @ApiResponse(code = 400, message = "[TR-001] 해당 유저는 이미 불꽃평가 등록을 완료했습니다. <br>" +
                    "[F-011] 완료된 필드가 아닙니다. <br>" +
                    "[F-012] 팀 멤버가 아닙니다.")
    })
    @PostMapping
    public ResponseEntity<String> postTeamworkRate(@RequestBody @Valid PostTeamworkRateReq postTeamworkRateReq, @AuthenticationPrincipal User user) {
        teamworkRateService.postTeamworkRate(postTeamworkRateReq, user);
        return ResponseDto.ok("불꽃평가 등록 완료");
    }
}
