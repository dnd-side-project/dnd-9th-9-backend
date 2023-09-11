package com.dnd.Exercise.domain.notification.repository;

import com.dnd.Exercise.domain.notification.entity.Notification;
import com.dnd.Exercise.domain.notification.entity.NotificationType;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
    Page<Notification> findByUserAndNotificationType(
            User user, NotificationType notificationType, Pageable pageable);

    @Modifying
    @Query(value =
            "update Notification n set n.isRead = true "
                    + "where n.user.id = :userId "
                    + "and n.isRead = false "
                    + "and n.notificationType = 'USER'")
    void bulkIsRead(@Param("userId") Long userId);
}
