package com.dnd.Exercise.domain.auth.controller;

import com.dnd.Exercise.domain.auth.dto.request.OAuthCodeReq;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "소셜 인증 🔐")
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @ApiOperation(value = "애플 로그인 🔐", notes = "인가 코드를 전송합니다. <br> 이미 존재하는 유저이면 로그인 / 새로운 유저이면 회원가입 처리 후 로그인 시켜줍니다.")
    @PostMapping("/apple-login")
    public ResponseEntity<TokenRes> appleLogin(@RequestBody OAuthCodeReq oAuthCodeReq) {
        return ResponseDto.ok(new TokenRes());
    }

    @ApiOperation(value = "카카오 로그인 🔐", notes = "인가 코드를 전송합니다. <br> 이미 존재하는 유저이면 로그인 / 새로운 유저이면 회원가입 처리 후 로그인 시켜줍니다.")
    @PostMapping("/kakao-login")
    public ResponseEntity<TokenRes> kakaoLogin(@RequestBody OAuthCodeReq oAuthCodeReq) {
        return ResponseDto.ok(new TokenRes());
    }

    @ApiOperation(value = "구글 로그인 🔐", notes = "인가 코드를 전송합니다. <br> 이미 존재하는 유저이면 로그인 / 새로운 유저이면 회원가입 처리 후 로그인 시켜줍니다.")
    @PostMapping("/google-login")
    public ResponseEntity<TokenRes> googleLogin(@RequestBody OAuthCodeReq oAuthCodeReq) {
        return ResponseDto.ok(new TokenRes());
    }
}
