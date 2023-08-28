package com.dnd.Exercise.domain.user.repository;

import com.dnd.Exercise.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);
    Optional<User> findById(long id);
    boolean existsByUid(String uid);
    List<User> findByIdIn(List<Long> ids);
}
