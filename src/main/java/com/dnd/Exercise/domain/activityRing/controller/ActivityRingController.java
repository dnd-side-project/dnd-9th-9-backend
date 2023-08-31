package com.dnd.Exercise.domain.activityRing.controller;

import com.dnd.Exercise.domain.activityRing.dto.request.UpdateActivityRingReq;
import com.dnd.Exercise.domain.activityRing.service.ActivityRingService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "활동링 💫")
@RestController
@RequiredArgsConstructor
@RequestMapping("/activity-ring")
public class ActivityRingController {

    private final ActivityRingService activityRingService;

    @ApiOperation(value = "활동링 정보 업데이트하기 💫", notes = "애플에서 업데이트된 활동링 정보를 반영합니다. " +
            "<br> - healthkit 의 activeEnergyBurned 값을 업데이트합니다. " +
            "<br> - 애플 연동 유저만 활동링을 업데이트 할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="활동링 정보 업데이트 완료"),
            @ApiResponse(code=400, message="애플 연동 유저만 활동링을 업데이트 할 수 있습니다.")
    })
    @PostMapping("")
    public ResponseEntity<String> updateActivityRing(@RequestBody UpdateActivityRingReq updateActivityRingReq, @AuthenticationPrincipal User user) {
        //TODO: 목표칼로리 달성 여부 확인 로직 추가
        //TODO: 애플의 목표 데이터는 background 추적이 불가능한데, 유저의 목표 칼로리 데이터는 언제 가져올 것인가? 매칭 시작 시점? << 이에 대한 정책 확립 필요

        activityRingService.updateActivityRing(updateActivityRingReq, user);
        return ResponseDto.ok("활동링 정보 업데이트 완료");
    }
}
