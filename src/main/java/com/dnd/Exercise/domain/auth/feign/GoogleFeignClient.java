package com.dnd.Exercise.domain.auth.feign;

import com.dnd.Exercise.domain.auth.dto.google.GoogleUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleClient", url = "https://oauth2.googleapis.com/tokeninfo")
public interface GoogleFeignClient {
    @PostMapping
    GoogleUser getUserInfo(@RequestParam("id_token") String idToken);
}