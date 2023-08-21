package com.dnd.Exercise.global.jwt;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 7 * 24 * 60 * 60 * 1000L)
public class RefreshToken {
    @Id
    private String refreshToken;
    @Indexed
    private Long userId;

    public RefreshToken(final String refreshToken, final Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
