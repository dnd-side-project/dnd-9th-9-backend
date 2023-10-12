package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.OAuthLoginReq;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;
import com.dnd.Exercise.domain.auth.feign.KakaoFeignClient;
import com.dnd.Exercise.domain.auth.dto.kakao.KakaoInfo;
import com.dnd.Exercise.domain.user.entity.LoginType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.dnd.Exercise.global.common.Constants.RANDOM_UID_BASE_STR;
import static com.dnd.Exercise.global.common.Constants.RANDOM_UID_CODE_LENGTH;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthServiceImpl implements OAuthService {

    private final KakaoFeignClient kakaoFeignClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public TokenRes kakaoLogin(OAuthLoginReq oAuthLoginReq) {
        String accessToken = oAuthLoginReq.getAccessToken();

        KakaoInfo kakaoInfo = getKakaoUserInfo(accessToken);
        String kakaoId = kakaoInfo.getId().toString();
        String nickname = kakaoInfo.getKakaoAccount().getProfile().getNickname();
        String email = kakaoInfo.getKakaoAccount().getEmail();

        User user = userRepository.findByOauthIdAndLoginType(kakaoId,LoginType.KAKAO).orElse(null);

        if (user == null) {
            String randomUid = generateRandomUid();
            user = userRepository.save(User.builder()
                            .uid(randomUid)
                            .name(nickname)
                            .email(email)
                            .loginType(LoginType.KAKAO)
                            .oauthId(kakaoId)
                            .isAppleLinked(false)
                            .isNotificationAgreed(true)
                            .build());
        }

        return TokenRes.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getId()))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getId()))
                .build();
    }

    private KakaoInfo getKakaoUserInfo(String accessToken) {
        try {
            return kakaoFeignClient.getInfo("bearer " + accessToken);
        } catch (Exception e) {
            log.error("error while getting kakao user info: ", e);
            throw new BusinessException(ErrorCode.INVALID_KAKAO_TOKEN);
        }
    }

    private String generateRandomUid() {
        Random rand = new Random();
        String uid;
        do {
            uid = RANDOM_UID_BASE_STR;
            String numStr = "";
            for (int i = 0; i < RANDOM_UID_CODE_LENGTH; i++) {
                String num = Integer.toString(rand.nextInt(10));
                numStr += num;
            }
            uid += numStr;
        } while (userRepository.existsByUid(uid));
        return uid;
    }
}
