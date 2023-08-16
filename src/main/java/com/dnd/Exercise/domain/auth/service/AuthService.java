package com.dnd.Exercise.domain.auth.service;

import com.dnd.Exercise.domain.auth.dto.request.SignUpReq;

public interface AuthService {
    void signUp(SignUpReq signUpReq);
}
