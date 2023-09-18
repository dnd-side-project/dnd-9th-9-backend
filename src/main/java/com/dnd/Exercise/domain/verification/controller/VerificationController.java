package com.dnd.Exercise.domain.verification.controller;

import com.dnd.Exercise.domain.verification.dto.request.SignUpCodeReq;
import com.dnd.Exercise.domain.verification.dto.request.VerifySignUpReq;
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

    @ApiOperation(value = "회원가입 시 인증번호 전송 요청 📞", notes = "해당 전화번호로 인증번호를 발송합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="인증번호 발송 완료")
    })
    @PostMapping("/sign-up-code")
    public ResponseEntity<String> signUpCode(@RequestBody @Valid SignUpCodeReq signUpCodeReq) {
        verificationService.signUpCode(signUpCodeReq);
        return ResponseDto.ok("인증번호 발송 완료");
    }

    @ApiOperation(value = "회원가입 시 인증번호 인증 📞", notes = "발송받은 인증번호로 인증을 수행합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="인증되었습니다."),
            @ApiResponse(code=400, message="잘못된 인증번호입니다. or 인증번호 유효시간이 지났습니다.")
    })
    @PostMapping("/verify/sign-up")
    public ResponseEntity<String> verifySignUp(@RequestBody @Valid VerifySignUpReq verifySignUpReq) {
        verificationService.verifySignUp(verifySignUpReq);
        return ResponseDto.ok("인증되었습니다.");
    }
}
