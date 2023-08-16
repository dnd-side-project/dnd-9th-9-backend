package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.SignUpReq;
import com.dnd.Exercise.domain.user.entity.LoginType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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
}
