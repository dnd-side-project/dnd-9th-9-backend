package com.dnd.Exercise.domain.verification.service;

import com.dnd.Exercise.domain.verification.dto.request.FindIdCodeReq;
import com.dnd.Exercise.domain.verification.dto.request.SignUpCodeReq;
import com.dnd.Exercise.domain.verification.dto.request.VerifyFindIdReq;
import com.dnd.Exercise.domain.verification.dto.request.VerifySignUpReq;
import com.dnd.Exercise.domain.verification.dto.response.VerifyFindIdRes;

public interface VerificationService {
    void signUpCode(SignUpCodeReq signUpCodeReq);
    void verifySignUp(VerifySignUpReq verifySignUpReq);
    void findIdCode(FindIdCodeReq findIdCodeReq);
    VerifyFindIdRes verifyFindId(VerifyFindIdReq verifyFindIdReq);
}
