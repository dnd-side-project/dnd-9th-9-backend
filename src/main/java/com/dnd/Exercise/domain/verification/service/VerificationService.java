package com.dnd.Exercise.domain.verification.service;

import com.dnd.Exercise.domain.verification.dto.request.SignUpCodeReq;
import com.dnd.Exercise.domain.verification.dto.request.VerifySignUpReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface VerificationService {
    void signUpCode(SignUpCodeReq signUpCodeReq);
    void verifySignUp(VerifySignUpReq verifySignUpReq);
}
