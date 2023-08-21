package com.dnd.Exercise.domain.auth.controller;

import com.dnd.Exercise.domain.auth.dto.request.LoginReq;
import com.dnd.Exercise.domain.auth.dto.request.RefreshReq;
import com.dnd.Exercise.domain.auth.dto.request.SignUpReq;
import com.dnd.Exercise.domain.auth.dto.response.AccessTokenRes;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;
import com.dnd.Exercise.domain.auth.service.AuthService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "인증 🔐")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "회원가입 🔐", notes = "일반 회원가입 시 사용합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="회원가입 완료"),
            @ApiResponse(code=400, message="이미 사용중인 아이디 입니다.")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpReq signUpReq) {
        if(!authService.checkUidAvailable(signUpReq.getUid())) {
            throw new BusinessException(ErrorCode.ID_ALREADY_EXISTS);
        }
        authService.signUp(signUpReq);
        return ResponseDto.ok("회원가입 완료");
    }

    @ApiOperation(value = "로그인 🔐", notes = "일반 로그인 시 사용합니다. <br> 발급받은 access 토큰은 추후 요청 시 Authorization 헤더에 'Bearer 토큰' 형태로 전송합니다.")
    @ApiResponses({
            @ApiResponse(code=400, message="아이디 또는 비밀번호를 잘못 입력하였습니다.")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenRes> login(@RequestBody @Valid LoginReq loginReq) {
        TokenRes token = authService.login(loginReq);
        return ResponseDto.ok(token);
    }

    @ApiOperation(value = "회원가입 시 중복된 id 체크 🔐", notes = "사용 가능한 id 인지 체크합니다.")
    @GetMapping("/id-available")
    public ResponseEntity<Boolean> idAvailable(@RequestParam String uid) {
        return ResponseDto.ok(authService.checkUidAvailable(uid));
    }

    @ApiOperation(value = "access 토큰 만료 시 재발급 🔐", notes = "refresh 토큰으로 access 토큰을 갱신합니다.")
    @ApiResponses({
            @ApiResponse(code=400, message="유효하지 않은 refresh 토큰 입니다.")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenRes> refresh(@RequestBody RefreshReq refreshReq) {
        AccessTokenRes token = authService.refresh(refreshReq);
        return ResponseDto.ok(token);
    }

    @ApiOperation(value = "로그아웃 🔐", notes = "")
    @ApiResponses({
            @ApiResponse(code=200, message="로그아웃 성공")
    })
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal User user) {
        authService.logout(user.getId());
        return ResponseDto.ok("로그아웃 성공");
    }

    // TODO: 아이디찾기, 비밀번호찾기(+수정) 추후 추가
}
