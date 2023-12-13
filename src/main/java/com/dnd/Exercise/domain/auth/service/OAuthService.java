package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.OAuthLoginReq;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;

public interface OAuthService {
    TokenRes kakaoLogin(OAuthLoginReq oAuthLoginReq);
    TokenRes googleLogin(OAuthLoginReq oAuthLoginReq);
}
