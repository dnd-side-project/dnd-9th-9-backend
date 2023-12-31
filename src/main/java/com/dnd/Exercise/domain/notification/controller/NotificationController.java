package com.dnd.Exercise.domain.notification.controller;

import com.dnd.Exercise.domain.notification.dto.response.FindFieldNotificationsRes;
import com.dnd.Exercise.domain.notification.dto.response.FindUserNotificationsRes;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "알림 💡")
@Slf4j
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "유저 알림 조회 💡")
    @ApiResponses({
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @GetMapping("/user")
    public ResponseEntity<FindUserNotificationsRes> findUserNotifications(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 10) Pageable pageable){
        FindUserNotificationsRes result = notificationService.findUserNotifications(user, pageable);
        return ResponseDto.ok(result);
    }


    @ApiOperation(value = "필드 알림 조회 💡")
    @GetMapping("/field/{id}")
    public ResponseEntity<FindFieldNotificationsRes> findFieldNotifications(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 ID") @PathVariable("id") Long id,
            @PageableDefault(size = 10) Pageable pageable){
        FindFieldNotificationsRes result = notificationService
                .findFieldNotifications(user, id, pageable);
        return ResponseDto.ok(result);
    }


    @ApiOperation(value = "알림 읽음 처리 💡")
    @ApiResponses({
            @ApiResponse(code=403, message="[C-006] 접근 권한이 없습니다."),
            @ApiResponse(code=404, message="[N-002] 알림을 찾을 수 없습니다.")
    })
    @PatchMapping("/{id}/read")
    public ResponseEntity<String> readNotification(
            @AuthenticationPrincipal User user,
            @Parameter(description = "알림 ID") @PathVariable("id") Long id){
        notificationService.readNotification(user, id);
        return ResponseDto.ok("알림 읽음");
    }

    @ApiOperation(value = "유저 알림 모두 읽기 💡")
    @PatchMapping("/user/read")
    public ResponseEntity<String> readAllNotifications(
            @AuthenticationPrincipal User user){
        notificationService.readAllNotifications(user);
        return ResponseDto.ok("알림 모두 읽음");
    }
}
