package com.dnd.Exercise.domain.notification.controller;

import com.dnd.Exercise.domain.notification.dto.response.FindAllNotificationsRes;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping("/user")
    public ResponseEntity<FindAllNotificationsRes> findAllUserNotifications(){
        FindAllNotificationsRes findAllNotificationsRes = new FindAllNotificationsRes();
        return ResponseDto.ok(findAllNotificationsRes);
    }


    @ApiOperation(value = "필드 알림 조회 💡")
    @GetMapping("/{id}")
    public ResponseEntity<FindAllNotificationsRes> findAllFieldNotifications(
            @Parameter(description = "필드 ID") @PathVariable("id") Long id){
        FindAllNotificationsRes findAllNotificationsRes = new FindAllNotificationsRes();
        return ResponseDto.ok(findAllNotificationsRes);
    }


    @ApiOperation(value = "알림 읽음 처리 💡")
    @PatchMapping("/{id}/read")
    public ResponseEntity<String> readNotification(
            @Parameter(description = "알림 ID") @PathVariable("id") Long id){
        return ResponseDto.ok("알림 읽음");
    }
}
