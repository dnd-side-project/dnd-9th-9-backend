package com.dnd.Exercise.domain.verification.controller;

import com.dnd.Exercise.domain.verification.dto.request.SendCodeReq;
import com.dnd.Exercise.domain.verification.dto.request.VerifyReq;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "전화번호 인증 📞")
@RestController
@RequestMapping("/verification")
public class VerificationController {

    @ApiOperation(value = "인증번호 전송 요청 📞", notes = "해당 전화번호로 인증번호를 발송합니다.")
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody SendCodeReq sendCodeReq) {
        return ResponseDto.ok("인증번호 발송 완료");
    }

    @ApiOperation(value = "인증번호 인증 📞", notes = "발송받은 인증번호로 인증을 수행합니다. 인증 성공 여부를 반환합니다.")
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(@RequestBody VerifyReq verifyReq) {
        return ResponseDto.ok(false);
    }
}
