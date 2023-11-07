package com.dnd.Exercise.domain.user.controller;

import com.dnd.Exercise.domain.user.dto.request.*;
import com.dnd.Exercise.domain.user.dto.response.GetProfileDetail;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.service.UserService;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "사용자 👤")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "test")
    @PostMapping("/{id}")
    public String testApi(@PathVariable int id, @RequestBody String name){
        try {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @ApiOperation(value = "내 정보 상세 조회 👤", notes = "'마이페이지' 사용")
    @GetMapping("/my/profile-detail")
    public ResponseEntity<GetProfileDetail> getProfileDetail(@AuthenticationPrincipal User user) {
        GetProfileDetail getProfileDetail = userService.getProfileDetail(user.getId());
        return ResponseDto.ok(getProfileDetail);
    }

    @ApiOperation(value = "온보딩 시 내 정보(신체정보) 등록 👤", notes = "[ 신체정보(몸무게,키) / 성별 / 애플 연동 여부 / (연동유저일 경우) 목표칼로리 ] 정보를 입력합니다. " +
            "<br> '애플 데이터와의 초기 연동' or '연동 안한 유저의 온보딩' 에서 사용합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="온보딩 정보 업데이트 완료"),
            @ApiResponse(code=400, message="애플 연동을 수행한 유저만 목표 칼로리를 설정할 수 있습니다."),
            @ApiResponse(code=400, message="애플 연동 유저인 경우 목표 칼로리 값을 전송해야 합니다.")
    })
    @PatchMapping("/my/onboard-profile")
    public ResponseEntity<String> updateOnboardProfile(@RequestBody @Valid UpdateOnboardProfileReq updateOnboardProfileReq, @AuthenticationPrincipal User user) {
        userService.updateOnboardProfile(updateOnboardProfileReq,user.getId());
        return ResponseDto.ok("온보딩 정보 업데이트 완료");
    }

    @ApiOperation(value = "온보딩 시 운동레벨 등록 👤", notes = "[ 일반로그인의 경우 ] 회원가입을 진행하며 운동레벨을 함께 등록하므로, 일반로그인 과정에서는 이 api 를 사용하지 않습니다. " +
            "<br> [ 소셜로그인의 경우 ] 소셜 회원가입 과정을 마친 뒤, 이 api 를 사용해 운동레벨을 추가로 등록합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="운동레벨 등록 완료")
    })
    @PostMapping("/my/skill-level")
    public ResponseEntity<String> postSkillLevel(@RequestBody @Valid PostSkillLevelReq postSkillLevelReq, @AuthenticationPrincipal User user) {
        userService.postSkillLevel(postSkillLevelReq,user.getId());
        return ResponseDto.ok("운동레벨 등록 완료");
    }

    @ApiOperation(value = "내 프로필 수정 👤", notes = "'마이페이지' 사용 <br> 목표 칼로리는 수정할 수 없다.")
    @ApiResponses({
            @ApiResponse(code=200, message="프로필 수정 완료")
    })
    @PatchMapping("/my/profile")
    public ResponseEntity<String> updateProfile(@ModelAttribute @Valid UpdateMyProfileReq updateMyProfileReq, @AuthenticationPrincipal User user) {
        userService.updateProfile(updateMyProfileReq,user.getId());
        return ResponseDto.ok("프로필 수정 완료");
    }

    @ApiOperation(value = "애플 연동 여부 수정 👤", notes = "애플 연동 설정/해제 시 '연동 여부 정보' 업데이트 " +
            "<br> - 초기 회원가입 시 서버에 '유저의 애플 연동 여부' 는 기본적으로 false 로 설정됩니다." +
            "<br> - 추후 유저가 애플 연동을 수행하거나, 되어있던 연동을 해제할 시 서버로 '연동 여부' 정보를 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="연동 정보 수정 완료")
    })
    @PatchMapping("/my/apple-linked")
    public ResponseEntity<String> updateAppleLinked(@RequestBody @Valid UpdateAppleLinkedReq updateAppleLinkedReq, @AuthenticationPrincipal User user) {
        userService.updateAppleLinked(updateAppleLinkedReq, user.getId());
        return ResponseDto.ok("연동 정보 수정 완료");
    }

    @ApiOperation(value = "알림 수신 여부 수정 👤", notes = "푸시알림 수신 여부를 설정합니다. 기본값은 true 로 설정되어 있습니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="알림 수신여부 수정 완료")
    })
    @PatchMapping("/my/notification-agreement")
    public ResponseEntity<String> updateNotificationAgreement(@RequestBody @Valid UpdateNotificationAgreementReq updateNotificationAgreementReq, @AuthenticationPrincipal User user) {
        userService.updateNotificationAgreed(updateNotificationAgreementReq, user.getId());
        return ResponseDto.ok("알림 수신여부 수정 완료");
    }
}
