package com.dnd.Exercise.domain.auth.repository;

import com.dnd.Exercise.global.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
    RefreshToken findByUserId(long userId);
}
