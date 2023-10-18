package com.dnd.Exercise.domain.auth.controller;

import com.dnd.Exercise.domain.auth.dto.request.*;
import com.dnd.Exercise.domain.auth.dto.response.AccessTokenRes;
import com.dnd.Exercise.domain.auth.dto.response.FindIdRes;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;
import com.dnd.Exercise.domain.auth.service.AuthService;
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
            @ApiResponse(code=400, message="[A-002] 이미 사용중인 아이디 입니다.")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpReq signUpReq) {
        authService.signUp(signUpReq);
        return ResponseDto.ok("회원가입 완료");
    }

    @ApiOperation(value = "로그인 🔐", notes = "일반 로그인 시 사용합니다. <br> 발급받은 access 토큰은 추후 요청 시 Authorization 헤더에 'Bearer 토큰' 형태로 전송합니다.")
    @ApiResponses({
            @ApiResponse(code=400, message="[A-001] 아이디 또는 비밀번호를 잘못 입력하였습니다.")
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
            @ApiResponse(code=401, message="[J-001] 유효하지 않은 JWT 토큰 입니다. or [J-002] 만료된 JWT 토큰입니다. or [J-003] 지원하지 않는 JWT 토큰입니다.")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenRes> refresh(@RequestBody @Valid RefreshReq refreshReq) {
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

    @ApiOperation(value = "아이디 찾기 🔐", notes = "이름과 전화번호를 통해 유저의 아이디를 찾아줍니다. <br>" +
            "- 해당 '이름 + 전화번호' 로 가입된 아이디가 여러 개일 경우, 아이디들을 모두 반환합니다. <br>" +
            "- 전화번호 인증을 완료한 유저일 경우에 한해서만 아이디 찾기가 가능합니다. <br>" +
            "- 전화번호 인증을 수행하지 않은 유저일 경우, '전화번호 인증이 사전 수행되어야 합니다.' 라는 오류메시지를 반환합니다.")
    @ApiResponses({
            @ApiResponse(code=400, message="[V-004] 전화번호 인증이 사전 수행되어야 합니다.")
    })
    @GetMapping("/find-id")
    public ResponseEntity<FindIdRes> findId(@ModelAttribute @Valid FindIdReq findIdReq) {
        FindIdRes findIdRes = authService.findId(findIdReq);
        return ResponseDto.ok(findIdRes);
    }

    @ApiOperation(value = "비밀번호 재설정 🔐", notes = "비밀번호를 재설정합니다. <br>" +
            "- '새로운 비밀번호' + '새로운 비밀번호 확인' 조합을 서버로 전송합니다. <br>" +
            "- 위의 두 조합이 서로 일치하고, 비밀번호 유효성 조건에 부합한다면 비밀번호를 재설정합니다. <br>" +
            "- 전화번호 인증을 완료한 유저일 경우에 한해서만 비밀번호 변경이 가능합니다. <br>" +
            "- 전화번호 인증을 수행하지 않은 유저일 경우, '전화번호 인증이 사전 수행되어야 합니다.' 라는 오류메시지를 반환합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="비밀번호가 재설정 되었습니다."),
            @ApiResponse(code=400, message="[A-003] 비밀번호가 일치하지 않습니다. or [V-004] 전화번호 인증이 사전 수행되어야 합니다.")
    })
    @PostMapping("/change-pw")
    public ResponseEntity<String> changePw(@RequestBody @Valid ChangePwReq changePwReq) {
        authService.changePw(changePwReq);
        return ResponseDto.ok("비밀번호가 재설정 되었습니다.");
    }
}
