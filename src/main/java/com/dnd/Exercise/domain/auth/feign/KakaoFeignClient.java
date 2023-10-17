package com.dnd.Exercise.domain.auth.feign;

import com.dnd.Exercise.domain.auth.dto.kakao.KakaoInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoClient", url = "https://kapi.kakao.com/v2/user/me")
public interface KakaoFeignClient {
    @PostMapping
    KakaoInfo getInfo(@RequestHeader("Authorization") String accessToken);
}
