package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.LoginReq;
import com.dnd.Exercise.domain.auth.dto.request.RefreshReq;
import com.dnd.Exercise.domain.auth.dto.request.SignUpReq;
import com.dnd.Exercise.domain.auth.dto.response.AccessTokenRes;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;

public interface AuthService {
    void signUp(SignUpReq signUpReq);
    TokenRes login(LoginReq loginReq);
    boolean checkUidAvailable(String uid);
    AccessTokenRes refresh(RefreshReq refreshReq);
}
