package com.dnd.Exercise.domain.auth.repository;

import com.dnd.Exercise.global.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
