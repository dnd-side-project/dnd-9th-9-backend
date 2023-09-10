package com.dnd.Exercise.domain.verification.controller;

import com.dnd.Exercise.domain.verification.dto.request.SendCodeReq;
import com.dnd.Exercise.domain.verification.dto.request.VerifyReq;
import com.dnd.Exercise.domain.verification.service.VerificationService;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody @Valid SendCodeReq sendCodeReq) {
        try {
            verificationService.sendSms(sendCodeReq);
        } catch (Exception e) {
            log.error("error while sending verification code: {}", e);
        }
        return ResponseDto.ok("인증번호 발송 완료");
    }

    @ApiOperation(value = "인증번호 인증 📞", notes = "발송받은 인증번호로 인증을 수행합니다. 인증 성공 여부를 반환합니다.")
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody @Valid VerifyReq verifyReq) {
        verificationService.verify(verifyReq);
        return ResponseDto.ok("인증되었습니다.");
    }
}
