package com.dnd.Exercise.domain.fcmToken.repository;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import com.dnd.Exercise.domain.user.entity.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends CrudRepository<FcmToken, Long> {

    List<FcmToken> findByUserIn(List<User> users);

    List<FcmToken> findByUser(User user);
}
