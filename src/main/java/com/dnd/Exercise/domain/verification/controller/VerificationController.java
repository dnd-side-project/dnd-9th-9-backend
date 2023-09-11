package com.dnd.Exercise.domain.verification.controller;

import com.dnd.Exercise.domain.verification.dto.request.SendCodeReq;
import com.dnd.Exercise.domain.verification.dto.request.VerifyReq;
import com.dnd.Exercise.domain.verification.service.VerificationService;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "전화번호 인증 📞")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/verification")
public class VerificationController {

    private final VerificationService verificationService;

    @ApiOperation(value = "인증번호 전송 요청 📞", notes = "해당 전화번호로 인증번호를 발송합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="인증번호 발송 완료")
    })
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody @Valid SendCodeReq sendCodeReq) {
        try {
            verificationService.sendSms(sendCodeReq);
        } catch (Exception e) {
            log.error("error while sending verification code: {}", e);
        }
        return ResponseDto.ok("인증번호 발송 완료");
    }

    @ApiOperation(value = "인증번호 인증 📞", notes = "발송받은 인증번호로 인증을 수행합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="인증되었습니다."),
            @ApiResponse(code=400, message="잘못된 인증번호입니다. or 인증번호 유효시간이 지났습니다.")
    })
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody @Valid VerifyReq verifyReq) {
        verificationService.verify(verifyReq);
        return ResponseDto.ok("인증되었습니다.");
    }
}
