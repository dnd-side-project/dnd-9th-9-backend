package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.*;
import com.dnd.Exercise.domain.auth.dto.response.AccessTokenRes;
import com.dnd.Exercise.domain.auth.dto.response.FindIdRes;
import com.dnd.Exercise.domain.auth.dto.response.TokenRes;

public interface AuthService {
    void signUp(SignUpReq signUpReq);
    TokenRes login(LoginReq loginReq);
    boolean checkUidAvailable(String uid);
    AccessTokenRes refresh(RefreshReq refreshReq);
    void logout(Long userId);
    FindIdRes findId(FindIdReq findIdReq);
    void changePw(ChangePwReq changePwReq);
}
