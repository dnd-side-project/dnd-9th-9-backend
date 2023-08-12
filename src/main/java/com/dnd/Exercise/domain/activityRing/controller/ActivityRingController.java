package com.dnd.Exercise.domain.activityRing.controller;

import com.dnd.Exercise.domain.activityRing.dto.UpdateActivityRingReq;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "활동링 💫")
@RestController
@RequestMapping("/activity-ring")
public class ActivityRingController {

    @ApiOperation(value = "활동링 정보 업데이트하기 💫", notes = "애플에서 업데이트된 활동링 정보를 반영합니다.")
    @PostMapping("")
    public ResponseEntity<String> updateActivityRing(@RequestBody UpdateActivityRingReq updateActivityRingReq) {
        return ResponseDto.ok("활동링 정보 업데이트 완료");
    }
}
