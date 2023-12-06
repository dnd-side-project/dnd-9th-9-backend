package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.OAuthLoginReq;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;
import com.dnd.Exercise.domain.auth.feign.GoogleFeignClient;
import com.dnd.Exercise.domain.auth.feign.KakaoFeignClient;
import com.dnd.Exercise.domain.auth.dto.kakao.KakaoUser;
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
import com.dnd.Exercise.domain.auth.dto.google.*;

import java.util.Random;

import static com.dnd.Exercise.global.common.Constants.RANDOM_UID_BASE_STR;
import static com.dnd.Exercise.global.common.Constants.RANDOM_UID_CODE_LENGTH;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthServiceImpl implements OAuthService {

    private final KakaoFeignClient kakaoFeignClient;
    private final GoogleFeignClient googleFeignClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public TokenRes kakaoLogin(OAuthLoginReq oAuthLoginReq) {
        String accessToken = oAuthLoginReq.getToken();

        KakaoUser kakaoUser = getKakaoUserInfo(accessToken);
        String kakaoId = kakaoUser.getId().toString();
        String nickname = kakaoUser.getKakaoAccount().getProfile().getNickname();
        String email = kakaoUser.getKakaoAccount().getEmail();

        User user = getUser(kakaoId, nickname, email, LoginType.KAKAO);

        return generateToken(user);
    }

    @Override
    @Transactional
    public TokenRes googleLogin(OAuthLoginReq oAuthLoginReq) {
        String idToken = oAuthLoginReq.getToken();

        GoogleUser googleUser = getGoogleUserInfo(idToken);
        String googleId = googleUser.getSub();
        String name = googleUser.getName();
        String email = googleUser.getEmail();

        User user = getUser(googleId, name, email, LoginType.GOOGLE);

        return generateToken(user);
    }

    private KakaoUser getKakaoUserInfo(String accessToken) {
        try {
            return kakaoFeignClient.getUserInfo("bearer " + accessToken);
        } catch (Exception e) {
            log.error("error while getting kakao user info: ", e);
            throw new BusinessException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    private GoogleUser getGoogleUserInfo(String idToken) {
        try {
            return googleFeignClient.getUserInfo(idToken);
        } catch (Exception e) {
            log.error("error while getting google user info: ", e);
            throw new BusinessException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    private User getUser(String oauthId, String name, String email, LoginType loginType) {
        User user = userRepository.findByOauthIdAndLoginType(oauthId, loginType).orElse(null);
        if (user == null) {
            user = createUser(oauthId, name, email, loginType);
        }
        return user;
    }

    private User createUser(String oauthId, String name, String email, LoginType loginType) {
        String randomUid = generateRandomUid();
        return userRepository.save(User.builder()
                    .uid(randomUid)
                    .name(name)
                    .email(email)
                    .loginType(loginType)
                    .oauthId(oauthId)
                    .isAppleLinked(false)
                    .isNotificationAgreed(true)
                    .build());
    }

    private TokenRes generateToken(User user) {
        return TokenRes.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getId()))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getId()))
                .build();
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
