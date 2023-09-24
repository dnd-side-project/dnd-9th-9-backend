package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.LoginReq;
import com.dnd.Exercise.domain.auth.dto.request.RefreshReq;
import com.dnd.Exercise.domain.auth.dto.request.SignUpReq;
import com.dnd.Exercise.domain.auth.dto.response.AccessTokenRes;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;
import com.dnd.Exercise.domain.auth.repository.RefreshTokenRedisRepository;
import com.dnd.Exercise.domain.user.entity.LoginType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.jwt.JwtTokenProvider;
import com.dnd.Exercise.global.jwt.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final VerificationService verificationService;

    @Override
    @Transactional
    public void signUp(SignUpReq signUpReq) {
        verificationService.validateIsVerified(signUpReq.getPhoneNum(), VerifyingType.SIGN_UP);

        if(!checkUidAvailable(signUpReq.getUid())) {
            throw new BusinessException(ErrorCode.ID_ALREADY_EXISTS);
        }

        userRepository.save(User.builder()
                .uid(signUpReq.getUid())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .phoneNum(signUpReq.getPhoneNum())
                .name(signUpReq.getName())
                .skillLevel(signUpReq.getSkillLevel())
                .loginType(LoginType.MATCH_UP)
                .isAppleLinked(false)
                .isNotificationAgreed(true)
                .build());
    }

    @Override
    public TokenRes login(LoginReq loginReq) {
        User user = userRepository.findByUid(loginReq.getUid()).orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));
        if (!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        TokenRes token = TokenRes.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getId()))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getId()))
                .build();
        return token;
    }

    @Override
    public boolean checkUidAvailable(String uid) {
        return !userRepository.existsByUid(uid);
    }

    @Override
    public AccessTokenRes refresh(RefreshReq refreshReq) {
        String requestToken = refreshReq.getRefreshToken();

        if (requestToken == null || !jwtTokenProvider.validateToken(requestToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        RefreshToken redisToken = refreshTokenRedisRepository.findById(requestToken).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));
        if ((redisToken.getUserId() != Long.parseLong(jwtTokenProvider.getUserId(requestToken)))) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        AccessTokenRes newAccessToken = AccessTokenRes.builder()
                .accessToken(jwtTokenProvider.createAccessToken(redisToken.getUserId()))
                .build();
        return newAccessToken;
    }

    @Override
    public void logout(Long userId) {
        RefreshToken token = refreshTokenRedisRepository.findByUserId(userId);
        refreshTokenRedisRepository.deleteById(token.getRefreshToken());
    }
}
