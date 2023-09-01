package com.dnd.Exercise.domain.notification.repository;

import com.dnd.Exercise.domain.notification.entity.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

}
