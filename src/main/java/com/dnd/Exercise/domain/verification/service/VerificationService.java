package com.dnd.Exercise.domain.verification.service;

import com.dnd.Exercise.domain.verification.dto.request.SendCodeReq;
import com.dnd.Exercise.domain.verification.dto.request.VerifyReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface VerificationService {
    void sendSms(SendCodeReq sendCodeReq) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException;
    void verify(VerifyReq verifyReq);
}
