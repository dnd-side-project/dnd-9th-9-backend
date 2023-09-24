package com.dnd.Exercise.domain.verification.service;

import com.dnd.Exercise.domain.verification.dto.VerifyingType;
import com.dnd.Exercise.domain.verification.dto.request.*;

public interface VerificationService {
    void signUpCode(SignUpCodeReq signUpCodeReq);
    void findIdCode(FindIdCodeReq findIdCodeReq);
    void verify(VerifyReq verifyReq);
    void validateIsVerified(String phoneNum, VerifyingType verifyingType);
}
