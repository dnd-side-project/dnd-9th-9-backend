package com.dnd.Exercise.domain.verification.service;

import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.verification.dto.request.*;
import com.dnd.Exercise.domain.verification.dto.response.NaverSmsRes;
import com.dnd.Exercise.domain.verification.dto.response.VerifyFindIdRes;
import com.dnd.Exercise.global.common.RedisService;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerificationServiceImpl implements VerificationService {
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final long VERIFICATION_CODE_VALID_MINUTE = 10;

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String senderPhone;

    private final RedisService redisService;

    private final UserRepository userRepository;

    @Override
    public void signUpCode(SignUpCodeReq signUpCodeReq) {
        try {
            sendSms(signUpCodeReq.getPhoneNum());
        } catch (Exception e) {
            log.error("error while sending verification code: {}", e);
        }
    }

    @Override
    public void verifySignUp(VerifySignUpReq verifySignUpReq) {
        verify(verifySignUpReq.getPhoneNum(), verifySignUpReq.getCode());
    }

    @Override
    public void findIdCode(FindIdCodeReq findIdCodeReq) {
        String name = findIdCodeReq.getName();
        String phoneNum = findIdCodeReq.getPhoneNum();

        if (!userRepository.existsByNameAndPhoneNum(name,phoneNum)) {
            throw new BusinessException(ErrorCode.UNEXISTING_USER);
        }

        try {
            sendSms(phoneNum);
        } catch (Exception e) {
            log.error("error while sending verification code: {}", e);
        }
    }

    @Override
    public VerifyFindIdRes verifyFindId(VerifyFindIdReq verifyFindIdReq) {
        String phoneNum = verifyFindIdReq.getPhoneNum();
        String code = verifyFindIdReq.getCode();
        String name = verifyFindIdReq.getName();

        verify(phoneNum, code);

        List<User> users = userRepository.findAllByNameAndPhoneNum(name,phoneNum);
        List<String> uids = users.stream().map(User::getUid).collect(Collectors.toList());

        return VerifyFindIdRes.builder()
                .uids(uids)
                .build();
    }

    private void sendSms(String receiverPhone) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        String verificationCode = makeRandomNumber();

        log.info("receiver phone number: {}", receiverPhone);
        log.info("verification code: {}", verificationCode);

        redisService.setValues(receiverPhone, verificationCode, Duration.ofMinutes(VERIFICATION_CODE_VALID_MINUTE));

        String messageContent = new StringBuilder()
                .append("[매치업] ")
                .append(verificationCode)
                .append(" 인증번호를 입력해주세요.")
                .append("\n")
                .append("인증번호 유효시간은 " + VERIFICATION_CODE_VALID_MINUTE + "분입니다.")
                .toString();

        List<MessageDto> messages = new ArrayList<>();
        messages.add(MessageDto.builder()
                .to(receiverPhone)
                .content(messageContent)
                .build());

        NaverSmsReq request = NaverSmsReq.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(senderPhone)
                .content(messageContent)
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        NaverSmsRes response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, NaverSmsRes.class);
    }

    private void verify(String phoneNum, String requestCode) {
        if (!redisService.hasKey(phoneNum)) {
            throw new BusinessException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        String verificationCode = redisService.getValues(phoneNum);
        if (!verificationCode.equals(requestCode)) {
            throw new BusinessException(ErrorCode.INCORRECT_VERIFICATION_CODE);
        }

        redisService.deleteValues(phoneNum);
    }

    private String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

        return encodeBase64String;
    }

    private String makeRandomNumber() {
        Random rand = new Random();
        String numStr = "";
        for(int i=0; i<VERIFICATION_CODE_LENGTH; i++) {
            String num = Integer.toString(rand.nextInt(10));
            numStr += num;
        }
        return numStr;
    }
}
