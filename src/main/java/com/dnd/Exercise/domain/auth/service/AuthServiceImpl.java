package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.LoginReq;
import com.dnd.Exercise.domain.auth.dto.request.SignUpReq;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;
import com.dnd.Exercise.domain.user.entity.LoginType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.jwt.JwtTokenProvider;
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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public void signUp(SignUpReq signUpReq) {
        userRepository.save(User.builder()
                .uid(signUpReq.getUid())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .phoneNum(signUpReq.getPhoneNum())
                .name(signUpReq.getName())
                .skillLevel(signUpReq.getSkillLevel())
                .loginType(LoginType.MATCH_UP)
                .build());
    }

    @Override
    public TokenRes login(LoginReq loginReq) {
        User user = userRepository.findByUid(loginReq.getUid()).orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));
        if (!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        TokenRes token = TokenRes.builder()
                .accessToken(jwtTokenProvider.createToken(user.getId()))
                .build();
        return token;
    }

    @Override
    public boolean checkUidAvailable(String uid) {
        return !userRepository.existsByUid(uid);
    }
}
